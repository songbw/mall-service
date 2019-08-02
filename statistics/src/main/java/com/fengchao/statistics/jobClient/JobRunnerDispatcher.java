package com.fengchao.statistics.jobClient;

import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import com.fengchao.statistics.utils.JSONUtil;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.spring.boot.annotation.JobRunner4TaskTracker;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Robert HG (254963746@qq.com) on 4/9/16.
 */
@JobRunner4TaskTracker
@Slf4j
public class JobRunnerDispatcher implements JobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobRunnerDispatcher.class);

    private static final ConcurrentHashMap<String, JobRunner> JOB_RUNNER_MAP = new ConcurrentHashMap<String, JobRunner>();

    static {
        JOB_RUNNER_MAP.put("orderStatistics", new OrderStatisticsRunnerJobImpl());
    }

    @Override
    public Result run(JobContext jobContext) throws Throwable {
        // sleuth 自定义trace begin TODO : 目前MDC不设置会导致当前xiancheng5不打印traceid
        CurrentTraceContext currentTraceContext = CurrentTraceContext.Default.create();
        currentTraceContext.newScope(TraceContext.newBuilder().traceId(new Date().getTime()).spanId(1L).build());
        MDC.put("X-B3-TraceId", currentTraceContext.get().traceIdString());
        // sleuth 自定义trace begin

        log.info("接收到执行任务:{}", JSONUtil.toJsonString(jobContext));

        Job job = jobContext.getJob();
        String type = job.getParam("type");

        if (type != null && !"".equals(type)) {
            return JOB_RUNNER_MAP.get(type).run(jobContext);
        } else {
            LOGGER.info("type is null.");
            return new Result(Action.EXECUTE_FAILED, "type is null.");
        }
    }
}
