package com.fengchao.statistics.service.impl;

import com.fengchao.statistics.bean.vo.MerchantCityRangeStatisticResVo;
import com.fengchao.statistics.constants.StatisticPeriodTypeEnum;
import com.fengchao.statistics.dao.MCityOrderAmountDao;
import com.fengchao.statistics.model.MCityOrderamount;
import com.fengchao.statistics.rpc.VendorsRpcService;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import com.fengchao.statistics.rpc.extmodel.SysCompany;
import com.fengchao.statistics.rpc.extmodel.SysUser;
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
    private MCityOrderAmountDao mCityOrderAmountDao;

    @Override
    public void statisticDailyOrderAmoutByCity(List<OrderDetailBean> orderDetailBeanList, String startDateTime, String endDateTime) throws Exception {
        // 1. 根据商户id维度将订单详情分类
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

        // 2. 获取商户名称
        Set<Integer> merchantIdSet = orderDetailBeansByMerchantMap.keySet(); // 商户id集合
        List<SysCompany> sysCompanyList = vendorsRpcService.queryMerchantByIdList(new ArrayList<>(merchantIdSet));
        // 转map key:merchantId  value:SysUser
        Map<Integer, SysCompany> sysCompanyMap = sysCompanyList.stream()
                .collect(Collectors.toMap(u -> u.getId().intValue(), u -> u));

        // 3. 获取统计数据
        String statisticsDateTime =
                DateUtil.calcDay(startDateTime, DateUtil.DATE_YYYY_MM_DD, 1, DateUtil.DATE_YYYY_MM_DD); // 统计时间

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
            for (String cityId : cityIdSet) {
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
                mCityOrderamount.setStatisticsDate(DateUtil.parseDateTime(statisticsDateTime, DateUtil.DATE_YYYY_MM_DD));
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
        int count = mCityOrderAmountDao.deleteCategoryOverviewByPeriodTypeAndStatisticDate(
                StatisticPeriodTypeEnum.DAY.getValue().shortValue(),
                DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS),
                DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
        log.info("按照商户-城市(天)维度统计订单详情总金额数据; 统计时间范围：{} - {} 删除数据数量:{}",
                startDateTime, endDateTime, count);

        // 4.2 执行插入
        for (MCityOrderamount mCityOrderamount : mCityOrderamoutList) {
            mCityOrderAmountDao.insertCategoryOverview(mCityOrderamount);
        }

        log.info("按照商户-城市(天)维度统计订单详情总金额数据; 统计时间范围：{} - {} 执行完成!");
    }

    @Override
    public Map<String, List<MerchantCityRangeStatisticResVo>> fetchStatisticDailyResult(String startDate,
                                                                                        String endDate,
                                                                                        Integer merchantId) throws Exception {
        // 1. 查询数据库
        Date _startDate = DateUtil.parseDateTime(startDate, DateUtil.DATE_YYYY_MM_DD);
        Date _endDate = DateUtil.parseDateTime(endDate, DateUtil.DATE_YYYY_MM_DD);
        log.info("根据时间范围获取daily型的商户-城市(天)维度统计数据 日期范围: {} - {}, 商户id:{}", _startDate, _endDate, merchantId);
        List<MCityOrderamount> mCityOrderamountList =
                mCityOrderAmountDao.selectDailyCategoryOverviewsByDateRange(_startDate, _endDate);
        log.info("根据时间范围获取daily型的商户-城市(天)维度统计数据 数据库返回: {}", JSONUtil.toJsonString(mCityOrderamountList));

        // 2. 统计数据
        // 2.1 将获取到的数据按照'城市id'分组， 并转换成 MerchantCityRangeStatisticResVo
        Map<String, List<MerchantCityRangeStatisticResVo>> cityRangeMap = new HashMap<>();
        for (MCityOrderamount mCityOrderamount : mCityOrderamountList) {
            String cityId = mCityOrderamount.getCityId();
            // 转 MerchantCityRangeStatisticResVo
            MerchantCityRangeStatisticResVo merchantCityRangeStatisticResVo
                    = convertToMerchantCityRangeStatisticResVo(mCityOrderamount);

            List<MerchantCityRangeStatisticResVo> cityRangeList = cityRangeMap.get(cityId);
            if (cityRangeList == null) {
                cityRangeList = new ArrayList<>();
                cityRangeMap.put(cityId, cityRangeList);
            }
            cityRangeList.add(merchantCityRangeStatisticResVo);
        }

        // 2.2 将数据按照日期排序
        Set<String> cityIdSet = cityRangeMap.keySet();
        for (String cityId : cityIdSet) {
            List<MerchantCityRangeStatisticResVo> merchantCityRangeStatisticResVoList = cityRangeMap.get(cityId);

            merchantCityRangeStatisticResVoList.sort(Comparator.comparing(MerchantCityRangeStatisticResVo::getStatisticDate));
        }

        log.info("根据时间范围获取daily型的商户-城市(天)维度统计数据 获取统计数据Map<String, List<MerchantCityRangeStatisticResVo>>:{}",
                JSONUtil.toJsonString(cityRangeMap));
        return cityRangeMap;
    }

    // ======================================== private ==========================================

    private MerchantCityRangeStatisticResVo convertToMerchantCityRangeStatisticResVo(MCityOrderamount mCityOrderamount) {
        MerchantCityRangeStatisticResVo merchantCityRangeStatisticResVo = new MerchantCityRangeStatisticResVo();

        merchantCityRangeStatisticResVo.setCityId(mCityOrderamount.getCityId());
        merchantCityRangeStatisticResVo.setCityName(mCityOrderamount.getCityName());
        merchantCityRangeStatisticResVo.setOrderAmount(new BigDecimal(mCityOrderamount.getOrderAmount())
                .divide(new BigDecimal(100)).toString()); // 单位：元
        merchantCityRangeStatisticResVo.setStatisticDate(DateUtil.
                dateTimeFormat(mCityOrderamount.getStatisticStartTime(), DateUtil.DATE_YYYYMMDD)); // 日期 20190101

        return merchantCityRangeStatisticResVo;
    }
}
