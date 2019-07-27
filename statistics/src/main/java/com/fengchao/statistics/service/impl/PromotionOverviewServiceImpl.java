package com.fengchao.statistics.service.impl;

import com.fengchao.statistics.bean.vo.PromotionOverviewResVo;
import com.fengchao.statistics.constants.StatisticPeriodTypeEnum;
import com.fengchao.statistics.dao.PromotionOverviewDao;
import com.fengchao.statistics.feign.OrderServiceClient;
import com.fengchao.statistics.model.PromotionOverview;
import com.fengchao.statistics.rpc.EquityRpcService;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import com.fengchao.statistics.rpc.extmodel.PromotionBean;
import com.fengchao.statistics.service.PromotionOverviewService;
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
public class PromotionOverviewServiceImpl implements PromotionOverviewService {

    @Autowired
    private OrderServiceClient orderService;

    @Autowired
    private EquityRpcService equityRpcService;

    @Autowired
    private PromotionOverviewDao promotionOverviewDao;

    @Override
    public void doDailyStatistic(List<OrderDetailBean> orderDetailBeanList , String startDateTime, String endDateTime) throws Exception {
        // 1. 根据活动id维度将订单详情分类
        Map<Integer, List<OrderDetailBean>> orderDetailBeansByPromotionMap = new HashMap<>();
        for (OrderDetailBean orderDetailBean : orderDetailBeanList) {
            Integer promotionId = orderDetailBean.getPromotionId(); // 活动id
            if (promotionId == null || promotionId == 0) { // 无活动的订单
                promotionId = -1;
            }
            List<OrderDetailBean> _orderDetailBeanList = orderDetailBeansByPromotionMap.get(promotionId);
            if (_orderDetailBeanList == null) {
                _orderDetailBeanList = new ArrayList<>();
                orderDetailBeansByPromotionMap.put(promotionId, _orderDetailBeanList);
            }
            _orderDetailBeanList.add(orderDetailBean);
        }

        // 2. 获取活动名称
        Set<Integer> promotionIdSet = orderDetailBeansByPromotionMap.keySet(); // 活动id集合
        List<PromotionBean> promotionBeanList = equityRpcService.queryPromotionByIdList(new ArrayList<>(promotionIdSet));
        // 转map key:promotionId  value:PromotionBean
        Map<Integer, PromotionBean> promotionBeanMap = promotionBeanList.stream()
                .collect(Collectors.toMap(p -> p.getId().intValue(), p -> p));

        // 3. 获取统计数据
        String statisticsDateTime =
                DateUtil.calcDay(startDateTime, DateUtil.DATE_YYYY_MM_DD, 1, DateUtil.DATE_YYYY_MM_DD); // 统计时间
        List<PromotionOverview> promotionOverviewList = new ArrayList<>(); // 统计数据
        for (Integer promotionId : promotionIdSet) { // 遍历map
            List<OrderDetailBean> _orderDetailBeanList = orderDetailBeansByPromotionMap.get(promotionId);

            BigDecimal orderAmount = new BigDecimal(0);
            for (OrderDetailBean orderDetailBean : _orderDetailBeanList) {
                BigDecimal _tmpPrice = new BigDecimal(orderDetailBean.getSaleAmount());
                orderAmount = orderAmount.add(_tmpPrice);
            }

            PromotionOverview promotionOverview = new PromotionOverview();

            promotionOverview.setPromotionId(promotionId);
            promotionOverview.setPromotionName(promotionBeanMap.get(promotionId) == null ?
                    "其他" : promotionBeanMap.get(promotionId).getName());
            promotionOverview.setStatisticsDate(DateUtil.parseDateTime(statisticsDateTime, DateUtil.DATE_YYYY_MM_DD));
            promotionOverview.setStatisticStartTime(DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
            promotionOverview.setStatisticEndTime(DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
            promotionOverview.setPeriodType(StatisticPeriodTypeEnum.DAY.getValue().shortValue());
            promotionOverview.setOrderAmount(orderAmount.multiply(new BigDecimal(100)).longValue());
        }


        log.info("按照活动维度统计订单详情总金额数据; 统计时间范围：{} - {} 统计结果:{}",
                startDateTime, endDateTime, JSONUtil.toJsonString(promotionOverviewList));

        // 4. 插入统计数据
        // 4.1 首先按照“统计时间”和“统计类型”从数据库获取是否有已统计过的数据; 如果有，则删除
        promotionOverviewDao.deleteCategoryOverviewByPeriodTypeAndStatisticDate(
                StatisticPeriodTypeEnum.DAY.getValue().shortValue(),
                DateUtil.parseDateTime(statisticsDateTime, DateUtil.DATE_YYYY_MM_DD));

        // 4.2 执行插入
        for (PromotionOverview promotionOverview : promotionOverviewList) {
            promotionOverviewDao.insertCategoryOverview(promotionOverview);
        }

        log.info("按照活动维度统计订单详情总金额数据; 统计时间范围：{} - {} 执行完成!");
    }

    @Override
    public List<PromotionOverviewResVo> fetchStatisticDailyResult(String startDate, String endDate) throws Exception {
        // 1. 查询数据库
        Date _startDate = DateUtil.parseDateTime(startDate, DateUtil.DATE_YYYY_MM_DD);
        Date _endDate = DateUtil.parseDateTime(endDate, DateUtil.DATE_YYYY_MM_DD);
        log.info("根据时间范围获取daily型的活动维度统计数据 日期范围: {} - {}", _startDate, _endDate);
        List<PromotionOverview> promotionOverviewList =
                promotionOverviewDao.selectDailyCategoryOverviewsByDateRange(_startDate, _endDate);
        log.info("根据时间范围获取daily型的活动维度统计数据 数据库返回: {}", JSONUtil.toJsonString(promotionOverviewList));

        // 2. 将获取到的数据按照活动分组
        Map<Integer, List<PromotionOverview>> promotionOverviewListMap = new HashMap<>();
        for (PromotionOverview promotionOverview : promotionOverviewList) {
            Integer promotionId = promotionOverview.getPromotionId(); // 活动id

            List<PromotionOverview> _promotionOverviewList = promotionOverviewListMap.get(promotionId);
            if (_promotionOverviewList == null) {
                _promotionOverviewList = new ArrayList<>();
                promotionOverviewListMap.put(promotionId, _promotionOverviewList);
            }

            _promotionOverviewList.add(promotionOverview);
        }

        // 3. 组装统计数据 PromotionOverviewResVo
        List<PromotionOverviewResVo> promotionOverviewResVoList = new ArrayList<>();
        Set<Integer> promotionIdSet = promotionOverviewListMap.keySet();
        for (Integer promotionId : promotionIdSet) {
            List<PromotionOverview> _promotionOverviewList = promotionOverviewListMap.get(promotionId);

            String promotionName = ""; // 活动名称
            Long totalAmount = 0L; // 单位：分
            for (PromotionOverview promotionOverview : _promotionOverviewList) {
                totalAmount = totalAmount + promotionOverview.getOrderAmount();
                promotionName = promotionOverview.getPromotionName();
            }

            PromotionOverviewResVo promotionOverviewResVo = new PromotionOverviewResVo();
            promotionOverviewResVo.setPromotionId(promotionId);
            promotionOverviewResVo.setPromotionName(promotionName);
            promotionOverviewResVo.setOrderPaymentAmount(new BigDecimal(totalAmount).divide(new BigDecimal(100)).toString());

            promotionOverviewResVoList.add(promotionOverviewResVo);
        }

        log.info("根据时间范围获取daily型的活动维度统计数据 获取统计数据List<PromotionOverviewResVo>:{}",
                JSONUtil.toJsonString(promotionOverviewResVoList));

        return promotionOverviewResVoList;
    }


}
