package com.fengchao.statistics.jobClient;

import com.fengchao.statistics.bean.Promotion;
import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.feign.OrderService;
import com.fengchao.statistics.feign.WorkOrdersService;
import com.fengchao.statistics.model.PeriodOverview;
import com.fengchao.statistics.service.MerchantOverviewService;
import com.fengchao.statistics.service.OverviewService;
import com.fengchao.statistics.service.PeriodOverviewService;
import com.fengchao.statistics.service.PromotionOverviewService;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class OrderStatisticsRunnerJobImpl implements JobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatisticsRunnerJobImpl.class);


    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();
            Calendar calendar = new GregorianCalendar();
            Date date = new Date() ;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
            String startDate = formatter.format(new Date()) ;
            calendar.setTime(date);
            calendar.add(calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动
            date=calendar.getTime(); //这个时间就是日期往后推一天的结果
            String endDate = formatter.format(date) ;

            // TODO 业务逻辑
            LOGGER.info("我要执行订单统计操作：" + startDate + " 到 " + endDate + jobContext);
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
    }
}
