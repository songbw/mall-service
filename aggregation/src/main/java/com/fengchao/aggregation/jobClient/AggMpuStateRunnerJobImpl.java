package com.fengchao.aggregation.jobClient;


import com.fengchao.aggregation.service.AggregationService;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;


public class AggMpuStateRunnerJobImpl implements JobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggMpuStateRunnerJobImpl.class);


    @Override
    public Result run(JobContext jobContext) throws Throwable {
        try {
            BizLogger bizLogger = jobContext.getBizLogger();

            // 业务逻辑
            LOGGER.info("我要执行更新聚合页MPU操作：" + jobContext);
            AggregationService service = BeanContext.getApplicationContext().getBean(AggregationService.class);
            service.updateMpuPriceAndStateForAggregation();
        } catch (Exception e) {
            LOGGER.info("Run job failed!", e);
            return new Result(Action.EXECUTE_FAILED, e.getMessage());
        }
        return new Result(Action.EXECUTE_SUCCESS, "执行成功了，哈哈");
    }
}
