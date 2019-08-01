package com.fengchao.statistics.jobClient;

import com.alibaba.fastjson.JSON;
import com.fengchao.statistics.rpc.OrdersRpcService;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import com.fengchao.statistics.service.CategoryOverviewService;
import com.fengchao.statistics.service.MerchantOverviewService;
import com.fengchao.statistics.service.PeriodOverviewService;
import com.fengchao.statistics.service.PromotionOverviewService;
import com.fengchao.statistics.utils.DateUtil;
import com.fengchao.statistics.utils.JSONUtil;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.List;

@Slf4j
public class OrderStatisticsRunnerJobImpl implements JobRunner {

    /**
     * 按照分类统计任务
     */
    private static final String CATEGORY = "category";

    /**
     * 按照商户统计任务
     */
    private static final String MERCHANT = "merchant";

    /**
     * 按照活动统计任务
     */
    private static final String PROMOTION = "promotion";

    /**
     * 按照时间段统计任务
     */
    private static final String PERIOD = "period";


    /**
     * 统计任务-每天凌晨1点执行，统计前一天的数据
     * 1. 按品类统计订单支付总额
     * 2. 按商户统计订单支付总额
     * 3. 按活动统计订单数量
     * 4. 按时间段统计订单总额
     *
     * @param jobContext
     * @return
     * @throws Throwable
     */
    @Override
    public Result run(JobContext jobContext) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("总统计时间");
        log.info("执行平台订单统计任务开始:{}", JSONUtil.toJsonString(jobContext));

        String tasks = jobContext.getJob().getParam("tasks");
        List<String> needExecuteTaskList = JSON.parseArray(tasks, String.class);
        log.info("执行平台订单统计任务 需要执行的任务:{}", JSONUtil.toJsonString(needExecuteTaskList));

        String startDateTime = "", endDateTime = "";
        try {
            // 1.处理查询的时间范围
            // 当前日期
            String currentDate = DateUtil.nowDate(DateUtil.DATE_YYYY_MM_DD);
            // 开始时间
            startDateTime =
                    DateUtil.calcDay(currentDate + " 00:00:00", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS, -1, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            endDateTime =
                    DateUtil.calcSecond(startDateTime, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS, (60 * 60 * 24 - 1), DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

            log.info("执行订单统计任务 统计时间范围 startDateTime:{} - endDateTime:{}", startDateTime, endDateTime);

            // 2. 获取各个统计service
            CategoryOverviewService categoryOverviewService =
                    BeanContext.getApplicationContext().getBean(CategoryOverviewService.class);
            MerchantOverviewService merchantOverviewService =
                    BeanContext.getApplicationContext().getBean(MerchantOverviewService.class);
            PromotionOverviewService promotionOverviewService =
                    BeanContext.getApplicationContext().getBean(PromotionOverviewService.class);
            PeriodOverviewService periodOverviewService =
                    BeanContext.getApplicationContext().getBean(PeriodOverviewService.class);
            OrdersRpcService ordersRpcService =
                    BeanContext.getApplicationContext().getBean(OrdersRpcService.class);

            // 3. 统计
            // 3.1 调用order rpc服务，根据时间范围查询已支付的订单详情
            List<OrderDetailBean> payedOrderDetailBeanList =
                    ordersRpcService.queryPayedOrderDetailList(startDateTime, endDateTime);

            log.info("执行平台订单统计任务 获取的日统计数据List<OrderDetailBean>:{}", JSONUtil.toJsonString(payedOrderDetailBeanList));

            // 3.2执行统计
            // overviewService.add(queryBean); // 总揽统计
            if (needExecuteTaskList.contains(CATEGORY) && CollectionUtils.isNotEmpty(payedOrderDetailBeanList)) {
                categoryOverviewService.doDailyStatistic(payedOrderDetailBeanList, startDateTime, endDateTime); // 安品类统计
            }

            if (needExecuteTaskList.contains(MERCHANT) && CollectionUtils.isNotEmpty(payedOrderDetailBeanList)) {
                merchantOverviewService.doDailyStatistic(payedOrderDetailBeanList, startDateTime, endDateTime); // 按商户统计订单支付总额
            }

            if (needExecuteTaskList.contains(PROMOTION)) {
                promotionOverviewService.doDailyStatistic(payedOrderDetailBeanList, startDateTime, endDateTime); // 按照活动统计订单数量
            }

            if (needExecuteTaskList.contains(PERIOD)) {
                periodOverviewService.doStatistic(payedOrderDetailBeanList, startDateTime, endDateTime); // 按照时间段统计订单支付总额
            }
        } catch (Exception e) {
            log.error("执行平台订单统计任务异常:{}", e.getMessage(), e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }

        stopWatch.stop();
        log.info(stopWatch.prettyPrint());

        // 4. 记录lts日志
        BizLogger bizLogger = jobContext.getBizLogger();
        bizLogger.info(startDateTime + " 到 " + endDateTime + "的日订单统计完成");

        log.info("执行平台订单统计任务完成");
        return new Result(Action.EXECUTE_SUCCESS, "执行平台订单统计任务完成");
    }
}
