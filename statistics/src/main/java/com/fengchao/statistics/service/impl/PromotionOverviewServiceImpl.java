package com.fengchao.statistics.service.impl;

import com.fengchao.statistics.bean.PromotionTypeResDto;
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
import org.apache.commons.collections4.CollectionUtils;
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
    public void doDailyStatistic(List<OrderDetailBean> orderDetailBeanList,
                                 String startDateTime, String endDateTime) throws Exception {
        // 0. 获取活动类别信息
        // 获取所有活动类型，准备插入空数据
        List<PromotionTypeResDto> promotionTypeResDtoList = equityRpcService.queryAllPromotionTypeList();
        // 准备空数据， 以'活动类别'维度生成空统计数据的map
        Map<String, List<OrderDetailBean>> orderDetailBeansByPromotionTypeMap = new HashMap<>(); // !!
        for (PromotionTypeResDto promotionTypeResDto : promotionTypeResDtoList) {
            orderDetailBeansByPromotionTypeMap.put(promotionTypeResDto.getTypeName(), new ArrayList<>());
        }

        // 1. 获取活动信息
        // 获取所有订单详情的活动id集合
        Set<Integer> promotionIdSet =
                orderDetailBeanList.stream().map(o -> o.getPromotionId()).collect(Collectors.toSet());
        // 获取活动信息
        List<PromotionBean> promotionBeanList = equityRpcService.queryPromotionByIdList(new ArrayList<>(promotionIdSet));
        // 转map key:promotionId  value:PromotionBean
        Map<Integer, PromotionBean> promotionBeanMap = promotionBeanList.stream()
                .collect(Collectors.toMap(p -> p.getId().intValue(), p -> p));


        // 2. 根据'活动类别'维度将订单详情分类 活动类型 1:秒杀 2:优选 3:普通
        for (OrderDetailBean orderDetailBean : orderDetailBeanList) {
            // 从订单中获取活动id
            Integer promotionId = orderDetailBean.getPromotionId();
            if (promotionId == null || promotionId == 0) { // 无活动的订单
                promotionId = -1;
            }

            // 获取该订单的活动类型
            String promotionTypeName = "其他";
            PromotionBean promotionBean = promotionBeanMap.get(promotionId);
            if (promotionBean != null) { // 活动类型 1:秒杀 2:优选 3:普通
                promotionTypeName = promotionBean.getTypeName();
            }

            List<OrderDetailBean> _orderDetailBeanList = orderDetailBeansByPromotionTypeMap.get(promotionTypeName);
            if (_orderDetailBeanList == null) {
                _orderDetailBeanList = new ArrayList<>();
                orderDetailBeansByPromotionTypeMap.put(promotionTypeName, _orderDetailBeanList);
            }
            _orderDetailBeanList.add(orderDetailBean);
        }

        // 3. 获取统计数据
        String statisticsDateTime =
                DateUtil.calcDay(startDateTime, DateUtil.DATE_YYYY_MM_DD, 1, DateUtil.DATE_YYYY_MM_DD); // 统计时间
        List<PromotionOverview> promotionOverviewList = new ArrayList<>(); // 统计数据
        Set<String> promotionTypeSet = orderDetailBeansByPromotionTypeMap.keySet();
        for (String promotionTypeName : promotionTypeSet) { // 遍历
            List<OrderDetailBean> _orderDetailBeanList = orderDetailBeansByPromotionTypeMap.get(promotionTypeName);

            Integer orderCount = 0; // 统计订单数量
            for (OrderDetailBean orderDetailBean : _orderDetailBeanList) {
                orderCount++;
            }

            PromotionOverview promotionOverview = new PromotionOverview();

            promotionOverview.setPromotionType(promotionTypeName);
            promotionOverview.setOrderCount(orderCount);

            promotionOverview.setStatisticsDate(DateUtil.parseDateTime(statisticsDateTime, DateUtil.DATE_YYYY_MM_DD));
            promotionOverview.setStatisticStartTime(DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
            promotionOverview.setStatisticEndTime(DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));
            promotionOverview.setPeriodType(StatisticPeriodTypeEnum.DAY.getValue().shortValue());
        }


        log.info("按照活动(天)维度统计订单详情总金额数据; 统计时间范围：{} - {} 统计结果:{}",
                startDateTime, endDateTime, JSONUtil.toJsonString(promotionOverviewList));

        // 4. 插入统计数据
        // 4.1 首先按照“统计时间”和“统计类型”从数据库获取是否有已统计过的数据; 如果有，则删除
        promotionOverviewDao.deleteCategoryOverviewByPeriodTypeAndStatisticDate(
                StatisticPeriodTypeEnum.DAY.getValue().shortValue(),
                DateUtil.parseDateTime(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS),
                DateUtil.parseDateTime(endDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS));

        // 4.2 执行插入
        for (PromotionOverview promotionOverview : promotionOverviewList) {
            promotionOverviewDao.insertCategoryOverview(promotionOverview);
        }

        log.info("按照活动(天)维度统计订单详情总金额数据; 统计时间范围：{} - {} 执行完成!");
    }

    @Override
    public Map<String, String> fetchStatisticDailyResult(String startDate, String endDate) throws Exception {
        // 1. 查询数据库
        Date _startDate = DateUtil.parseDateTime(startDate, DateUtil.DATE_YYYY_MM_DD);
        Date _endDate = DateUtil.parseDateTime(endDate, DateUtil.DATE_YYYY_MM_DD);
        log.info("根据时间范围获取daily型的活动维度统计数据 日期范围: {} - {}", _startDate, _endDate);
        List<PromotionOverview> promotionOverviewList =
                promotionOverviewDao.selectDailyCategoryOverviewsByDateRange(_startDate, _endDate);
        log.info("根据时间范围获取daily型的活动维度统计数据 数据库返回: {}", JSONUtil.toJsonString(promotionOverviewList));

        // 根据start时间排序
        promotionOverviewList.sort(Comparator.comparing(PromotionOverview::getStatisticStartTime));

        // 2. 统计数据
        // 2.1 按照日期维度分组
        Map<String, List<PromotionOverview>> dateRangeMap = new HashMap<>(); // 按照日期维度分组
        for (PromotionOverview promotionOverview : promotionOverviewList) {
            String _date = DateUtil.dateTimeFormat(
                    promotionOverview.getStatisticStartTime(), DateUtil.DATE_YYYY_MM_DD); // 活动name

            List<PromotionOverview> dateRangeList = dateRangeMap.get(_date);
            if (dateRangeList == null) {
                dateRangeList = new ArrayList<>();
                dateRangeMap.put(_date, dateRangeList);
            }

            dateRangeList.add(promotionOverview);
        }

        // 2.2 将数据转换成前端需要的格式：{[date:'', '秒杀':8, '优选':9 ...] ...}
        Map<String, String> resultMap = new HashMap<>();
        Set<String> dateSet = dateRangeMap.keySet();
        for (String date : dateSet) {
            resultMap.put("date", date);

            List<PromotionOverview> _promotionOverviewList = dateRangeMap.get(date);
            for (PromotionOverview promotionOverview : _promotionOverviewList) {
                resultMap.put(promotionOverview.getPromotionType(), promotionOverview.getOrderCount() + "");
            }
        }

        log.info("根据时间范围获取daily型的活动维度统计数据 获取统计数据Map<String, List<PromotionOverviewResVo>>:{}",
                JSONUtil.toJsonString(resultMap));

        return resultMap;
    }

    //====================================== private ======================================

    /**
//     * @param promotionOverview
//     * @return
//     */
//    private PromotionOverviewResVo convertToPromotionOverviewResVo(PromotionOverview promotionOverview) {
//        PromotionOverviewResVo promotionOverviewResVo = new PromotionOverviewResVo();
//
//        private String date; // '2019/6/1',
//
//
//        String promotionTypeName = promotionOverview.getPromotionType();
//
//        private Integer secKill = 0;
//        private Integer premium = 0;
//        private Integer normal = 0;
//        private Integer others = 0;
//
//        return promotionOverviewResVo;
//    }


}
