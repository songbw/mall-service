package com.fengchao.statistics.service.impl;

import com.fengchao.statistics.bean.vo.MOverallResVo;
import com.fengchao.statistics.bean.vo.MerchantCityRangeStatisticResVo;
import com.fengchao.statistics.constants.StatisticPeriodTypeEnum;
import com.fengchao.statistics.dao.MCityOrderAmountDao;
import com.fengchao.statistics.dao.MOverviewDao;
import com.fengchao.statistics.model.MCityOrderamount;
import com.fengchao.statistics.model.MOverview;
import com.fengchao.statistics.rpc.OrdersRpcService;
import com.fengchao.statistics.rpc.VendorsRpcService;
import com.fengchao.statistics.rpc.WorkOrdersRpcService;
import com.fengchao.statistics.rpc.extmodel.DayStatisticsBean;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import com.fengchao.statistics.rpc.extmodel.SysCompany;
import com.fengchao.statistics.rpc.extmodel.WorkOrder;
import com.fengchao.statistics.service.MerchantStatisticService;
import com.fengchao.statistics.utils.DateUtil;
import com.fengchao.statistics.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author tom
 * @Date 19-7-27 下午4:23
 */
@Service
@Slf4j
public class MerchantStatisticServiceImpl implements MerchantStatisticService {

    @Autowired
    private VendorsRpcService vendorsRpcService;

    @Autowired
    private WorkOrdersRpcService workOrdersRpcService;

    @Autowired
    private MOverviewDao mOverviewDao;

    @Autowired
    private MCityOrderAmountDao mCityOrderAmountDao;

    @Autowired
    private OrdersRpcService ordersRpcService;

    @Override
    @Deprecated
    public void statisticOverall(List<OrderDetailBean> payedOrderDetailBeanList,
                                 String startDateTime, String endDateTime, Date statisticDate) throws Exception {
        try {
            // 1. 根据商户id维度将订单详情分组
            Map<Integer, List<OrderDetailBean>> orderDetailBeansByMerchantMap = divideOrderDetailByMerchantId(payedOrderDetailBeanList);

            log.info("按天统计各个商户的整体运营数据 统计时间范围：{} - {}, 根据商户id维度将订单详情分组结果:{}",
                    startDateTime, endDateTime, JSONUtil.toJsonString(orderDetailBeansByMerchantMap));

            // 2. 获取商户名称
            Set<Integer> merchantIdSet = orderDetailBeansByMerchantMap.keySet(); // 商户id集合
            List<SysCompany> sysCompanyList = vendorsRpcService.queryMerchantByIdList(new ArrayList<>(merchantIdSet));
            // 转map key:merchantId  value:SysUser
            Map<Integer, SysCompany> sysCompanyMap = sysCompanyList.stream()
                    .collect(Collectors.toMap(u -> u.getId().intValue(), u -> u));

            // 3. 组装统计数据 :
            // 3.1 退货信息
            List<WorkOrder> workOrderList = workOrdersRpcService.queryRefundInfoList(startDateTime, endDateTime);
            // 转map key:merchantId, value:退货单数
            Map<Long, Integer> refundMap = new HashMap<>();
            for (WorkOrder workOrder : workOrderList) {
                Integer refundCount = refundMap.get(workOrder.getMerchantId());
                if (refundCount == null) {
                    refundCount = 0;
                }

                refundCount = refundCount + 1;
                refundMap.put(workOrder.getMerchantId(), refundCount);
            }
            log.info("按天统计各个商户的整体运营数据 统计时间范围：{} - {}, 获取的退货单数信息Map<Long, Integer>:{}",
                    startDateTime, endDateTime, JSONUtil.toJsonString(refundMap));


            // 3.2 订单 支付总额, 订单总量, 下单人数, 退货单数
            List<MOverview> mOverviewList = new ArrayList<>();
            for (Integer merchantId : merchantIdSet) { // 遍历以商户为维度的数据map
                // 某商户下的订单详情集合
                List<OrderDetailBean> _orderDetailBeanList = orderDetailBeansByMerchantMap.get(merchantId);

                Set<String> orderTradeNoSet = new HashSet<>(); // 主订单tradeNo集合-用于订单总量计算
                Set<String> openIdSet = new HashSet<>(); // 下单人id的集合-用于计算 下单人数
                BigDecimal orderAmountB = new BigDecimal(0); // 订单支付总金额

                for (OrderDetailBean _orderDetailBean : _orderDetailBeanList) {
                    // 拼装主订单tradeNo集合
                    if (_orderDetailBean.getTradeNo() != null) {
                        orderTradeNoSet.add(_orderDetailBean.getTradeNo());
                    }

                    // 拼装下单人id的集合
                    if (_orderDetailBean.getOpenId() != null) {
                        openIdSet.add(_orderDetailBean.getOpenId());
                    }

                    // 订单支付总额
                    Float saleAmount = _orderDetailBean.getSaleAmount();
                    if (saleAmount != null) {
                        orderAmountB = orderAmountB.add(new BigDecimal(saleAmount));
                    }
                }

                // 组装统计数据
                MOverview mOverview = new MOverview();
                mOverview.setMerchantId(merchantId);
                mOverview.setMerchantCode("");
                mOverview.setMerchantName(sysCompanyMap.get(merchantId) == null ?
                        "/" : sysCompanyMap.get(merchantId).getName());
                mOverview.setOrderAmount(orderAmountB.multiply(new BigDecimal(100)).longValue());
                mOverview.setOrderCount(orderTradeNoSet.size());
                mOverview.setOrderUserCount(openIdSet.size());
                mOverview.setRefundOrderCount(refundMap.get(merchantId) == null ? 0 : refundMap.get(merchantId)); // 退货单数
                mOverview.setStatisticStartTime(DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
                mOverview.setStatisticEndTime(DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
                mOverview.setPeriodType(StatisticPeriodTypeEnum.DAY.getValue().shortValue());
                mOverview.setStatisticsDate(statisticDate);

                mOverviewList.add(mOverview);
            }

            log.info("按天统计各个商户的整体运营数据 统计时间范围：{} - {} 获取的统计数据是:{}",
                    startDateTime, endDateTime, JSONUtil.toJsonString(mOverviewList));

            // 4. 统计数据入库
            // 4.1 首先按照“统计时间”和“统计类型”从数据库获取是否有已统计过的数据; 如果有，则删除
            int count = mOverviewDao.deleteByPeriodTypeAndStatisticDate(
                    StatisticPeriodTypeEnum.DAY.getValue().shortValue(),
                    DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS),
                    DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));

            log.info("按天统计各个商户的整体运营数据; 统计时间范围：{} - {} 删除数据条数:{}",
                    startDateTime, endDateTime, count);

            // 4.2 执行插入
            for (MOverview mOverview : mOverviewList) {
                mOverviewDao.insertMOverview(mOverview);
            }
        } catch (Exception e) {
            log.error("按天统计各个商户的整体运营数据; 统计时间范围：{} - {} 异常:{}",
                    startDateTime, endDateTime, e.getMessage(), e);
        }
    }

    @Override
    public void statisticDailyOrderAmountByCity(List<OrderDetailBean> orderDetailBeanList,
                                                String startDateTime,
                                                String endDateTime, Date statisticDate) throws Exception {
        try {
            // 1. 根据商户id维度将订单详情分类
            Map<Integer, List<OrderDetailBean>> orderDetailBeansByMerchantMap = divideOrderDetailByMerchantId(orderDetailBeanList);
            log.info("按照商户-城市(天)维度统计订单详情总金额数据; 统计时间范围：{} - {}, 根据商户id维度将订单详情分组结果:{}",
                    startDateTime, endDateTime, JSONUtil.toJsonString(orderDetailBeansByMerchantMap));

            // 2. 获取商户名称
            Set<Integer> merchantIdSet = orderDetailBeansByMerchantMap.keySet(); // 商户id集合
            List<SysCompany> sysCompanyList = vendorsRpcService.queryMerchantByIdList(new ArrayList<>(merchantIdSet));
            // 转map key:merchantId  value:SysUser
            Map<Integer, SysCompany> sysCompanyMap = sysCompanyList.stream()
                    .collect(Collectors.toMap(u -> u.getId().intValue(), u -> u));

            // 3. 获取统计数据
            // // 统计数据
            List<MCityOrderamount> mCityOrderamoutList = new ArrayList<>();
            for (Integer merchantId : merchantIdSet) { // 遍历以商户为维度的数据map
                // 以商户为维度的数据列表
                List<OrderDetailBean> _orderDetailBeanList = orderDetailBeansByMerchantMap.get(merchantId);

                // 3.1 将“以商户为维度的数据列表”继续以cityId维度分组
                Map<String, List<OrderDetailBean>> cityRangeOrderDetailMap = new HashMap<>();
                for (OrderDetailBean orderDetailBean : _orderDetailBeanList) {
                    String cityId = orderDetailBean.getCityId();
                    List<OrderDetailBean> cityRangeOrderDetailList = cityRangeOrderDetailMap.get(cityId);
                    if (cityRangeOrderDetailList == null) {
                        cityRangeOrderDetailList = new ArrayList<>();
                        cityRangeOrderDetailMap.put(cityId, cityRangeOrderDetailList);
                    }
                    cityRangeOrderDetailList.add(orderDetailBean);
                }

                // 3.2 组装统计数据
                Set<String> cityIdSet = cityRangeOrderDetailMap.keySet();
                for (String cityId : cityIdSet) { // 遍历 cityRangeOrderDetailMap
                    List<OrderDetailBean> cityRangeOrderDetailList = cityRangeOrderDetailMap.get(cityId);

                    String cityName = ""; // 城市名称
                    BigDecimal orderAmount = new BigDecimal(0);
                    for (OrderDetailBean orderDetailBean : cityRangeOrderDetailList) {
                        BigDecimal _tmpPrice = new BigDecimal(orderDetailBean.getSaleAmount());
                        orderAmount = orderAmount.add(_tmpPrice);
                        cityName = orderDetailBean.getCityName();
                    }

                    // 组装数据
                    MCityOrderamount mCityOrderamount = new MCityOrderamount();
                    mCityOrderamount.setMerchantId(merchantId);
                    mCityOrderamount.setMerchantCode("");
                    mCityOrderamount.setMerchantName(sysCompanyMap.get(merchantId) == null ?
                            "/" : sysCompanyMap.get(merchantId).getName());
                    mCityOrderamount.setCityId(cityId);
                    mCityOrderamount.setCityName(cityName);
                    mCityOrderamount.setStatisticsDate(statisticDate);
                    mCityOrderamount.setStatisticStartTime(DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
                    mCityOrderamount.setStatisticEndTime(DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
                    mCityOrderamount.setPeriodType(StatisticPeriodTypeEnum.DAY.getValue().shortValue());
                    mCityOrderamount.setOrderAmount(orderAmount.multiply(new BigDecimal(100)).longValue());

                    mCityOrderamoutList.add(mCityOrderamount);
                }
            } // 组装统计数据end

            log.info("按照商户-城市(天)维度统计订单详情总金额数据; 统计时间范围：{} - {} 统计结果:{}",
                    startDateTime, endDateTime, JSONUtil.toJsonString(mCityOrderamoutList));

            // 4. 插入统计数据
            // 4.1 首先按照“统计时间”和“统计类型”从数据库获取是否有已统计过的数据; 如果有，则删除
            int count = mCityOrderAmountDao.deleteByPeriodTypeAndStatisticDate(
                    StatisticPeriodTypeEnum.DAY.getValue().shortValue(),
                    DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS),
                    DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
            log.info("按照商户-城市(天)维度统计订单详情总金额数据; 统计时间范围：{} - {} 删除数据数量:{}",
                    startDateTime, endDateTime, count);

            // 4.2 执行插入
            for (MCityOrderamount mCityOrderamount : mCityOrderamoutList) {
                mCityOrderAmountDao.insertMCityOrderAmount(mCityOrderamount);
            }

            log.info("按照商户-城市(天)维度统计订单详情总金额数据; 统计时间范围：{} - {} 执行完成!");
        } catch (Exception e) {
            log.error("按照商户-城市(天)维度统计订单详情总金额数据; 统计时间范围：{} - {} 异常:{}",
                    startDateTime, endDateTime, e.getMessage(), e);
        }
    }

    @Override
    public void statisticDailyUserCount(List<OrderDetailBean> payedOrderDetailBeanList, String startDateTime, String endDateTime, Date statisticDate) throws Exception {

    }

    @Override
    public MOverallResVo fetchOverAll(Integer merchantId) throws Exception {
        // 1. 查询订单相关的整体运营数据
        // a.获取订单支付总额 b.(已支付)订单总量 c.(已支付)下单人数
        DayStatisticsBean dayStatisticsBean = ordersRpcService.queryMerchantOrdersOverallStatistic(merchantId);

        // 2. 查询该商户的退货人数
        Integer refundUserCount = 0; // workOrdersRpcService.queryRefundInfoList();

        // 3. 组装返回数据
        MOverallResVo mOverallResVo = new MOverallResVo();
        mOverallResVo.setOrderAmount(new BigDecimal(dayStatisticsBean.getOrderPaymentAmount()).toString()); // 订单总额
        mOverallResVo.setOrderCount(dayStatisticsBean.getOrderCount()); // 订单总量
        mOverallResVo.setOrderUserCount(dayStatisticsBean.getOrderPeopleNum()); // 下单人数
        mOverallResVo.setRefundOrderCount(refundUserCount); // 退货单数

        log.info("查询商户整体运营数据 merchantId:{} 获取的统计数据为:{}", merchantId, JSONUtil.toJsonString(mOverallResVo));

        return mOverallResVo;
    }

    @Override
    public Map<String, List<MerchantCityRangeStatisticResVo>> fetchOrderAmountTrend(String startDate,
                                                                                    String endDate,
                                                                                    Integer merchantId) throws Exception {
        // 1. 查询数据库
        Date _startDate = DateUtil.parseDateTime(startDate + " 00:00:00", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        Date _endDate = DateUtil.parseDateTime(endDate + " 23:59:59", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        log.info("根据时间范围获取daily型的商户-城市(天)维度统计数据 日期范围: {} - {}, 商户id:{}", _startDate, _endDate, merchantId);
        List<MCityOrderamount> mCityOrderamountList =
                mCityOrderAmountDao.selectDailyStatisticByDateRange(_startDate, _endDate, merchantId);
        log.info("根据时间范围获取daily型的商户-城市(天)维度统计数据 数据库返回: {}", JSONUtil.toJsonString(mCityOrderamountList));

        // 2. 统计数据
        // 2.1 将获取到的数据按照'城市name'分组， 并转换成 MerchantCityRangeStatisticResVo
        Map<String, List<MerchantCityRangeStatisticResVo>> cityRangeMap = new HashMap<>();
        for (MCityOrderamount mCityOrderamount : mCityOrderamountList) {
            String cityName = mCityOrderamount.getCityName();
            List<MerchantCityRangeStatisticResVo> cityRangeList = cityRangeMap.get(cityName);
            if (cityRangeList == null) {
                cityRangeList = new ArrayList<>();
                cityRangeMap.put(cityName, cityRangeList);
            }

            // 转 MerchantCityRangeStatisticResVo
            MerchantCityRangeStatisticResVo merchantCityRangeStatisticResVo
                    = convertToMerchantCityRangeStatisticResVo(mCityOrderamount);
            cityRangeList.add(merchantCityRangeStatisticResVo);
        }

        // 2.2 补充缺失的日期数据(因为有的日期该商户可能没有统计数据)
        Set<String> cityNameSet = cityRangeMap.keySet();
        for (String cityName : cityNameSet) {
            //
            List<MerchantCityRangeStatisticResVo> merchantCityRangeStatisticResVoList = cityRangeMap.get(cityName);
            // 转map key:日期yyy-MM-dd  value:MerchantCityRangeStatisticResVo
            Map<String, MerchantCityRangeStatisticResVo> dateRangeMap =
                    merchantCityRangeStatisticResVoList.stream().collect(Collectors.toMap(m -> m.getStatisticDate(), m -> m));
            // merchantCityRangeStatisticResVoList.sort(Comparator.comparing(MerchantCityRangeStatisticResVo::getStatisticDate));

            // 补充后的数据
            List<MerchantCityRangeStatisticResVo> fullStatisticResVoList = new ArrayList<>();
            String currentDate = startDate; // 当前时间 yyyy-MM-dd
            while (DateUtil.compareDate(currentDate, DateUtil.DATE_YYYY_MM_DD, endDate, DateUtil.DATE_YYYY_MM_DD) <= 0) {
                MerchantCityRangeStatisticResVo merchantCityRangeStatisticResVo = dateRangeMap.get(currentDate); // yyyy-MM-dd
                if (merchantCityRangeStatisticResVo == null) {
                    merchantCityRangeStatisticResVo = new MerchantCityRangeStatisticResVo("", cityName, "0", currentDate);
                }

                fullStatisticResVoList.add(merchantCityRangeStatisticResVo);

                currentDate = DateUtil.calcDay(currentDate, DateUtil.DATE_YYYY_MM_DD, 1, DateUtil.DATE_YYYY_MM_DD);
            }

            // 重新设置数据
            cityRangeMap.put(cityName, fullStatisticResVoList);
        }

        log.info("根据时间范围获取daily型的商户-城市(天)维度统计数据 获取统计数据Map<String, List<MerchantCityRangeStatisticResVo>>:{}",
                JSONUtil.toJsonString(cityRangeMap));

        return cityRangeMap;
    }


    // ======================================== private ==========================================

    /**
     * @param mCityOrderamount
     * @return
     */
    private MerchantCityRangeStatisticResVo convertToMerchantCityRangeStatisticResVo(MCityOrderamount mCityOrderamount) {
        MerchantCityRangeStatisticResVo merchantCityRangeStatisticResVo = new MerchantCityRangeStatisticResVo();

        merchantCityRangeStatisticResVo.setCityId(mCityOrderamount.getCityId());
        merchantCityRangeStatisticResVo.setCityName(mCityOrderamount.getCityName());
        merchantCityRangeStatisticResVo.setOrderAmount(new BigDecimal(mCityOrderamount.getOrderAmount())
                .divide(new BigDecimal(100)).toString()); // 单位：元
        merchantCityRangeStatisticResVo.setStatisticDate(DateUtil.
                dateTimeFormat(mCityOrderamount.getStatisticStartTime(), DateUtil.DATE_YYYY_MM_DD)); // 日期 2019-01-01

        return merchantCityRangeStatisticResVo;
    }

    /**
     * 将订单按照商户订单分组
     *
     * @param orderDetailBeanList 订单详情列表
     * @return
     */
    private Map<Integer, List<OrderDetailBean>> divideOrderDetailByMerchantId(List<OrderDetailBean> orderDetailBeanList) {
        Map<Integer, List<OrderDetailBean>> orderDetailBeansByMerchantMap = new HashMap<>();
        for (OrderDetailBean orderDetailBean : orderDetailBeanList) {
            Integer merchantId = orderDetailBean.getMerchantId(); // 商户id
            List<OrderDetailBean> _orderDetailBeanList = orderDetailBeansByMerchantMap.get(merchantId);
            if (_orderDetailBeanList == null) {
                _orderDetailBeanList = new ArrayList<>();
                orderDetailBeansByMerchantMap.put(merchantId, _orderDetailBeanList);
            }
            _orderDetailBeanList.add(orderDetailBean);
        }

        return orderDetailBeansByMerchantMap;
    }
}
