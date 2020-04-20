package com.fengchao.equity.jobClient;

import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.spring.boot.annotation.JobRunner4TaskTracker;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Robert HG (254963746@qq.com) on 4/9/16.
 */

@JobRunner4TaskTracker
public class JobRunnerDispatcher implements JobRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobRunnerDispatcher.class);

    private static final ConcurrentHashMap<String, JobRunner> JOB_RUNNER_MAP = new ConcurrentHashMap<String, JobRunner>();

    static {
        JOB_RUNNER_MAP.put(JobTypeName.PROMOTION_EFFECTIVE, new PromotionEffectiveRunnerJobImpl());
        JOB_RUNNER_MAP.put(JobTypeName.PROMOTION_END, new PromotionEndRunnerJobImpl());

        JOB_RUNNER_MAP.put(JobTypeName.CARD_INVALID, new CardInvalidRunnerJobImpl());

        JOB_RUNNER_MAP.put(JobTypeName.COUPON_EFFECTIVE, new CouponEffectiveRunnerJobImpl());
        JOB_RUNNER_MAP.put(JobTypeName.COUPON_END, new CouponEndRunnerJobImpl());
        JOB_RUNNER_MAP.put(JobTypeName.COUPON_INVALID, new CouponInvalidRunnerJobImpl());
        JOB_RUNNER_MAP.put(JobTypeName.COUPON_RELEASE, new CouponReleaseRunnerJobImpl());
        JOB_RUNNER_MAP.put(JobTypeName.COUPON_USE_INVALID, new CouponUseInfoInvalidRunnerJobImpl());

        JOB_RUNNER_MAP.put(JobTypeName.VIRTUAL_INVALID, new VirtualInvalidRunnerJobImpl());

        JOB_RUNNER_MAP.put(JobTypeName.TEST_LTS, new TestLtsJobImpl());
    }




    @Override
    public Result run(JobContext jobContext) throws Throwable {
        if(null == jobContext){
            LOGGER.error("JobRunnerDispatcher jobContext is null");
            return new Result(Action.EXECUTE_FAILED, "jobContext is null");
        }
        Job job = jobContext.getJob();
        String type = job.getParam("type");
        JobRunner jobRunner = null;
        if (null != type && !type.isEmpty()) {
            LOGGER.info("JobRunnerDispatcher type={} jobContext={}",jobContext.toString());

            switch(type){
                case JobTypeName.PROMOTION_EFFECTIVE:
                    jobRunner = new PromotionEffectiveRunnerJobImpl();
                    break;
                case JobTypeName.PROMOTION_END:
                    jobRunner = new PromotionEndRunnerJobImpl();
                    break;
                case JobTypeName.COUPON_EFFECTIVE:
                    jobRunner = new CouponEffectiveRunnerJobImpl();
                    break;
                case JobTypeName.COUPON_END:
                    jobRunner = new CouponEndRunnerJobImpl();
                    break;
                case JobTypeName.COUPON_INVALID:
                    jobRunner = new CouponInvalidRunnerJobImpl();
                    break;
                case JobTypeName.VIRTUAL_INVALID:
                    jobRunner = new VirtualInvalidRunnerJobImpl();
                    break;
                case JobTypeName.COUPON_RELEASE:
                    jobRunner = new CouponReleaseRunnerJobImpl();
                    break;
                case JobTypeName.CARD_INVALID:
                    jobRunner = new CardInvalidRunnerJobImpl();
                    break;
                case JobTypeName.COUPON_USE_INVALID:
                    jobRunner = new CouponUseInfoInvalidRunnerJobImpl();
                    break;
                case JobTypeName.TEST_LTS:
                    jobRunner = new TestLtsJobImpl();
                    break;
                default:
                        break;

            }
        }
        if (null == jobRunner){
            LOGGER.info("JobRunnerDispatcher: type is null.");
            return new Result(Action.EXECUTE_FAILED, "type is null.");
        }else {
            return jobRunner.run(jobContext);
        }
    }
}
