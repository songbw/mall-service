package com.fengchao.equity.jobClient;

import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.spring.boot.annotation.JobRunner4TaskTracker;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Robert HG (254963746@qq.com) on 4/9/16.
 */
@JobRunner4TaskTracker
public class JobRunnerDispatcher implements JobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobRunnerDispatcher.class);

    private static final ConcurrentHashMap<String, JobRunner> JOB_RUNNER_MAP = new ConcurrentHashMap<String, JobRunner>();


    static {
        JOB_RUNNER_MAP.put("promotionEffective", new PromotionEffectiveRunnerJobImpl());
        JOB_RUNNER_MAP.put("promotionEnd", new PromotionEndRunnerJobImpl());
        JOB_RUNNER_MAP.put("couponEffective", new CouponEffectiveRunnerJobImpl());
        JOB_RUNNER_MAP.put("couponEnd", new CouponEndRunnerJobImpl());
        JOB_RUNNER_MAP.put("couponInvalid", new CouponInvalidRunnerJobImpl());
        JOB_RUNNER_MAP.put("virtualInvalid", new VirtualInvalidRunnerJobImpl());
    }

    @Override
    public Result run(JobContext jobContext) throws Throwable {
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
