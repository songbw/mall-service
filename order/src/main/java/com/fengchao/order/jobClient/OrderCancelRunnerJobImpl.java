package com.fengchao.order.jobClient;

import com.fengchao.order.model.Order;
import com.fengchao.order.service.OrderService;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;


public class OrderCancelRunnerJobImpl implements JobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelRunnerJobImpl.class);


    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();

            // TODO 业务逻辑
            LOGGER.info("我要执行订单取消操作：" + jobContext);
            String id = jobContext.getJob().getParam("orderId") ;
            OrderService orderService = BeanContext.getApplicationContext().getBean(OrderService.class);
            int orderId = Integer.parseInt(id) ;
            Order order = orderService.findById(orderId) ;
            if (order != null && order.getStatus() == 0) {
                orderService.cancel(orderId) ;
                // 会发送到 LTS (JobTracker上)
                bizLogger.info("订单取消成功");
            }
        } catch (Exception e) {
            LOGGER.info("Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "执行成功了，哈哈");
    }
}
