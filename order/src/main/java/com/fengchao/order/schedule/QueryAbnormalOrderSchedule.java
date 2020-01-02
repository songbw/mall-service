package com.fengchao.order.schedule;

import com.fengchao.order.dao.OrdersDao;
import com.fengchao.order.model.Orders;
import com.fengchao.order.utils.AlarmUtil;
import com.fengchao.order.utils.DateUtil;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author tom
 * @Date 19-11-1 下午2:41
 */
@Component
@Slf4j
public class QueryAbnormalOrderSchedule {

    private static final Long expireTimeSeg = -60 * 32L; // 单位秒 32分钟

    private OrdersDao ordersDao;

    @Autowired
    public QueryAbnormalOrderSchedule(OrdersDao ordersDao) {
        this.ordersDao = ordersDao;
    }

    /**
     * 每2分钟检测一次
     */
    @Scheduled(fixedDelay = 120000)
    public void cancelRechargeOrder() {
        try {
            MDC.put("X-B3-TraceId", UUID.randomUUID().toString());

            log.info("获取未取消的异常订单任务开始 ==>");

            // 1. 获取超过"32"分钟未取消的订单

            // 目前时间
            String nowDateTime = DateUtil.nowDateTime(DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            // 获取比当前时间之前的 expireTimeSeg 这么多时间
            String expireTimeStr =
                    DateUtil.calcSecond(nowDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS,
                            expireTimeSeg, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

            log.info("获取未取消的异常订单任务 获取到的查询时间点是:{}", expireTimeStr);
            // 转date
            Date expireTimeDate = DateUtil.parseDateTime(expireTimeStr, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

            // 2.查询需要取消的充值记录
            List<Orders> ordersList = ordersDao.selectTimeoutAbnormal(expireTimeDate);

            log.info("获取未取消的异常订单任务 获取到的异常订单:{}", JSONUtil.toJsonString(ordersList));
            if (CollectionUtils.isEmpty(ordersList)) {
                log.info("获取未取消的异常订单任务 未获取到任务");

                return;
            }

            // 3. 执行取消任务
            List<Integer> orderIdList = ordersList.stream().map(o -> o.getId()).collect(Collectors.toList());

            // 4.报警
            AlarmUtil.alarmAsync("gat-获取未取消的异常订单任务", JSONUtil.toJsonString(orderIdList));

            log.info("获取未取消的异常订单任务 <==");
        } catch (Exception e) {
            log.info("获取未取消的异常订单任务:{}", e.getMessage(), e);

            // 报警
            AlarmUtil.alarmAsync("gat-获取未取消的异常订单任务", e.getMessage());
        }
    }
}
