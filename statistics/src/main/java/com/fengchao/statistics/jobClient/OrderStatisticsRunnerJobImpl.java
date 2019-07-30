package com.fengchao.statistics.jobClient;

import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.rpc.OrdersRpcService;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import com.fengchao.statistics.service.*;
import com.fengchao.statistics.utils.DateUtil;
import com.fengchao.statistics.utils.JSONUtil;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Slf4j
public class OrderStatisticsRunnerJobImpl implements JobRunner {

    /**
    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();
            Calendar calendar = new GregorianCalendar();
            Date date = new Date() ;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = formatter.format(new Date()) ;
            calendar.setTime(date);
            calendar.add(calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动
            date=calendar.getTime(); //这个时间就是日期往后推一天的结果
            String endDate = formatter.format(date) ;

            // TODO 业务逻辑
            LOGGER.info("我要执行订单统计操作：" + startDate + " 到 " + endDate + " : " + jobContext);
            OverviewService overviewService = BeanContext.getApplicationContext().getBean(OverviewService.class);
            MerchantOverviewService merchantOverviewService = BeanContext.getApplicationContext().getBean(MerchantOverviewService.class);
            PromotionOverviewService promotionOverviewService = BeanContext.getApplicationContext().getBean(PromotionOverviewService.class);
            PeriodOverviewService periodOverviewService = BeanContext.getApplicationContext().getBean(PeriodOverviewService.class);

            QueryBean queryBean = new QueryBean();
            queryBean.setStartTime(startDate);
            queryBean.setEndTime(endDate);

            overviewService.add(queryBean);
            merchantOverviewService.add(queryBean);
            promotionOverviewService.add(queryBean);
            periodOverviewService.add(queryBean);

            bizLogger.info(startDate + " 到 " + endDate + "的订单统计成功");
        } catch (Exception e) {
            LOGGER.info("订单统计Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "订单统计执行成功了，哈哈");
    } */

    /**
     * 统计任务-每天凌晨1点执行，统计前一天的数据
     * 1. 按品类统计订单支付总额
     * 2. 按商户统计订单支付总额
     * 3. 按活动统计订单支付总额
     * 4. 统计订单总额
     *
     * @param jobContext
     * @return
     * @throws Throwable
     */
    @Override
    public Result run(JobContext jobContext) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("执行订单统计任务开始:{}", JSONUtil.toJsonString(jobContext));

        try {
            // 1.处理查询的时间范围
            // 当前日期
            String currentDate = DateUtil.nowDate(DateUtil.DATE_YYYY_MM_DD);
            // 开始时间
            String startDateTime =
                    DateUtil.calcDay(currentDate + " 00:00:00", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS, -1, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
            String endDateTime =
                    DateUtil.calcSecond(startDateTime + " 23:59:59", DateUtil.DATE_YYYY_MM_DD_HH_MM_SS, (60 * 60 * 24 - 1), DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);

            // 2. 获取各个统计service
            OverviewService overviewService = BeanContext.getApplicationContext().getBean(OverviewService.class);
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
            // 组装条件
            QueryBean queryBean = new QueryBean();
            queryBean.setStartTime(startDateTime);
            queryBean.setEndTime(endDateTime);
            log.info("执行订单统计任务 统计查询条件:{}", JSONUtil.toJsonString(queryBean));

            // 3.1 调用order rpc服务，根据时间范围查询已支付的订单详情
            List<OrderDetailBean> payedOrderDetailBeanList =
                    ordersRpcService.queryPayedOrderDetailList(startDateTime, endDateTime);

            // 3.2执行统计
            // overviewService.add(queryBean); // 总揽统计
            categoryOverviewService.doDailyStatistic(payedOrderDetailBeanList, startDateTime, endDateTime); // 安品类统计
            merchantOverviewService.doDailyStatistic(payedOrderDetailBeanList, startDateTime, endDateTime); // 按商户统计订单支付总额
            promotionOverviewService.doDailyStatistic(payedOrderDetailBeanList, startDateTime, endDateTime); //
            periodOverviewService.add(queryBean);

            // 4. 记录lts日志
            BizLogger bizLogger = jobContext.getBizLogger();
            bizLogger.info(startDateTime + " 到 " + endDateTime + "的订单统计成功");
        } catch (Exception e) {
            log.error("执行订单统计任务异常:{}", e.getMessage(), e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }

        log.info("执行订单统计任务完成");
        return new Result(Action.EXECUTE_SUCCESS, "执行订单统计任务完成");
    }
}
