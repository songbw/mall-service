package com.fengchao.equity.jobClient;

import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.logger.BizLogger;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLtsJobImpl implements JobRunner {

    @Override
    public Result run(JobContext jobContext) {

       try {
           BizLogger bizLogger = jobContext.getBizLogger();

           log.info("我要执行LTS test操作：" + jobContext);
           bizLogger.info("LTS test成功");
           log.info("LTS test成功");
       }catch (Exception e) {
           log.error("Run job failed! {}",e.getMessage(), e);
           return new Result(Action.EXECUTE_FAILED, e.getMessage());
       }

        return new Result(Action.EXECUTE_SUCCESS, "LTS test执行成功");
    }
}
