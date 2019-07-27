package com.fengchao.statistics.service.impl;

import com.fengchao.statistics.bean.vo.MerchantOverviewResVo;
import com.fengchao.statistics.constants.StatisticPeriodTypeEnum;
import com.fengchao.statistics.dao.MerchantOverviewDao;
import com.fengchao.statistics.feign.ProductServiceClient;
import com.fengchao.statistics.model.MerchantOverview;
import com.fengchao.statistics.rpc.OrdersRpcService;
import com.fengchao.statistics.rpc.VendorsRpcService;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import com.fengchao.statistics.rpc.extmodel.SysUser;
import com.fengchao.statistics.service.MerchantOverviewService;
import com.fengchao.statistics.utils.DateUtil;
import com.fengchao.statistics.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantOverviewServiceImpl implements MerchantOverviewService {

    @Autowired
    private VendorsRpcService vendorsRpcService;

    @Autowired
    private MerchantOverviewDao merchantOverviewDao;

    @Override
    public void doDailyStatistic(List<OrderDetailBean> orderDetailBeanList , String startDateTime, String endDateTime) {
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
        List<SysUser> sysUserList = vendorsRpcService.queryMerchantByIdList(new ArrayList<>(merchantIdSet));
        // 转map key:merchantId  value:SysUser
        Map<Integer, SysUser> sysUserMap = sysUserList.stream()
                .collect(Collectors.toMap(u -> u.getId().intValue(), u -> u));

        // 3. 获取统计数据
        String statisticsDateTime =
                DateUtil.calcDay(startDateTime, DateUtil.DATE_YYYY_MM_DD, 1, DateUtil.DATE_YYYY_MM_DD); // 统计时间
        List<MerchantOverview> merchantOverviewList = new ArrayList<>(); // 统计数据
        for (Integer merchantId : merchantIdSet) { // 遍历map
            List<OrderDetailBean> _orderDetailBeanList = orderDetailBeansByMerchantMap.get(merchantId);

            BigDecimal orderAmount = new BigDecimal(0);
            for (OrderDetailBean orderDetailBean : _orderDetailBeanList) {
                BigDecimal _tmpPrice = new BigDecimal(orderDetailBean.getSaleAmount());
                orderAmount = orderAmount.add(_tmpPrice);
            }

            MerchantOverview merchantOverview = new MerchantOverview();


            merchantOverview.setMerchantId(merchantId);
            merchantOverview.setMerchantName(sysUserMap.get(merchantId) == null ?
                    "/" : sysUserMap.get(merchantId).getLoginName());
            merchantOverview.setStatisticsDate(DateUtil.parseDateTime(statisticsDateTime, DateUtil.DATE_YYYY_MM_DD));
            merchantOverview.setStatisticStartTime(DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
            merchantOverview.setStatisticEndTime(DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
            merchantOverview.setPeriodType(StatisticPeriodTypeEnum.DAY.getValue().shortValue());
            merchantOverview.setOrderAmount(orderAmount.multiply(new BigDecimal(100)).longValue());
        }


        log.info("按照商户(天)维度统计订单详情总金额数据; 统计时间范围：{} - {} 统计结果:{}",
                startDateTime, endDateTime, JSONUtil.toJsonString(merchantOverviewList));

        // 4. 插入统计数据
        // 4.1 首先按照“统计时间”和“统计类型”从数据库获取是否有已统计过的数据; 如果有，则删除
        merchantOverviewDao.deleteCategoryOverviewByPeriodTypeAndStatisticDate(
                StatisticPeriodTypeEnum.DAY.getValue().shortValue(),
                DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS),
                DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));

        // 4.2 执行插入
        for (MerchantOverview merchantOverview : merchantOverviewList) {
            merchantOverviewDao.insertCategoryOverview(merchantOverview);
        }

        log.info("按照商户(天)维度统计订单详情总金额数据; 统计时间范围：{} - {} 执行完成!");
    }

    @Override
    public List<MerchantOverviewResVo> fetchStatisticDailyResult(String startDate, String endDate) throws Exception {
        // 1. 查询数据库
        Date _startDate = DateUtil.parseDateTime(startDate, DateUtil.DATE_YYYY_MM_DD);
        Date _endDate = DateUtil.parseDateTime(endDate, DateUtil.DATE_YYYY_MM_DD);
        log.info("根据时间范围获取daily型的商户维度统计数据 日期范围: {} - {}", _startDate, _endDate);
        List<MerchantOverview> merchantOverviewList =
                merchantOverviewDao.selectDailyCategoryOverviewsByDateRange(_startDate, _endDate);
        log.info("根据时间范围获取daily型的商户维度统计数据 数据库返回: {}", JSONUtil.toJsonString(merchantOverviewList));

        // 2. 将获取到的数据按照商户分组
        Map<Integer, List<MerchantOverview>> merchantOverviewListMap = new HashMap<>();
        for (MerchantOverview merchantOverview : merchantOverviewList) {
            Integer merchantId = merchantOverview.getMerchantId(); // 商户id

            List<MerchantOverview> _merchantOverviewList = merchantOverviewListMap.get(merchantId);
            if (_merchantOverviewList == null) {
                _merchantOverviewList = new ArrayList<>();
                merchantOverviewListMap.put(merchantId, _merchantOverviewList);
            }

            _merchantOverviewList.add(merchantOverview);
        }

        // 3. 组装统计数据 MerchantOverviewResVo
        List<MerchantOverviewResVo> merchantOverviewResVoList = new ArrayList<>();
        Set<Integer> merchantIdSet = merchantOverviewListMap.keySet();
        for (Integer merchantId : merchantIdSet) {
            List<MerchantOverview> _merchantOverviewList = merchantOverviewListMap.get(merchantId);

            String merchantName = ""; // 商户名称
            Long totalAmount = 0L; // 单位：分
            for (MerchantOverview merchantOverview : _merchantOverviewList) {
                totalAmount = totalAmount + merchantOverview.getOrderAmount();
                merchantName = merchantOverview.getMerchantName();
            }

            MerchantOverviewResVo merchantOverviewResVo = new MerchantOverviewResVo();
            merchantOverviewResVo.setMerchantId(merchantId);
            merchantOverviewResVo.setMerchantName(merchantName);
            merchantOverviewResVo.setOrderPaymentAmount(new BigDecimal(totalAmount).divide(new BigDecimal(100)).toString());

            merchantOverviewResVoList.add(merchantOverviewResVo);
        }

        log.info("根据时间范围获取daily型的商户维度统计数据 获取统计数据List<MerchantOverviewResVo>:{}",
                JSONUtil.toJsonString(merchantOverviewResVoList));


        return merchantOverviewResVoList;
    }



}
