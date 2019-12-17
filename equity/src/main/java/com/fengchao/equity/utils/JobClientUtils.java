package com.fengchao.equity.utils;

import com.github.ltsopensource.core.commons.utils.DateUtils;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JobClientUtils {

    @Autowired
    private static Environment environment;

    /**
     * 活动生效任务
     * @param id
     */
    public static void promotionEffectiveTrigger(JobClient jobClient, Integer id, Date triggerTime) {
        Job job = new Job();
        job.setTaskId("promotion_effective_trigger_" + id);
        job.setParam("type", "promotionEffective");
        job.setParam("promotionId", id + "");
        job.setTaskTrackerNodeGroup("equity_trade_TaskTracker_" + environment.getActiveProfiles()[0]);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        job.setTriggerTime(triggerTime.getTime());   // 1 小时之后执行
        jobClient.submitJob(job);
    }

    /**
     * 活动结束任务
     * @param id
     */
    public static void promotionEndTrigger(JobClient jobClient, Integer id, Date triggerTime) {
        Job job = new Job();
        job.setTaskId("promotion_end_trigger_" + id);
        job.setParam("type", "promotionEnd");
        job.setParam("promotionId", id + "");
        job.setTaskTrackerNodeGroup("equity_trade_TaskTracker_" + environment.getActiveProfiles()[0]);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        job.setTriggerTime(triggerTime.getTime());   // 1 小时之后执行
        jobClient.submitJob(job);
    }

    /**
     * 优惠卷生效任务
     * @param id
     */
    public static void couponEffectiveTrigger(JobClient jobClient, Integer id, Date triggerTime) {
        Job job = new Job();
        job.setTaskId("coupon_effective_trigger_" + id);
        job.setParam("type", "couponEffective");
        job.setParam("couponId", id + "");
        job.setTaskTrackerNodeGroup("equity_trade_TaskTracker_" + environment.getActiveProfiles()[0]);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        job.setTriggerTime(triggerTime.getTime());   // 1 小时之后执行
        jobClient.submitJob(job);
    }

    /**
     * 优惠券结束任务
     * @param id
     */
    public static void couponEndTrigger(JobClient jobClient, Integer id, Date triggerTime) {
        Job job = new Job();
        job.setTaskId("coupon_end_trigger_" + id);
        job.setParam("type", "couponEnd");
        job.setParam("couponId", id + "");
        job.setTaskTrackerNodeGroup("equity_trade_TaskTracker_" + environment.getActiveProfiles()[0]);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        job.setTriggerTime(triggerTime.getTime());   // 1 小时之后执行
        jobClient.submitJob(job);
    }

    /**
     * 用户优惠券失效任务
     * @param id
     */
    public static void couponInvalidTrigger(JobClient jobClient, Integer id, Date triggerTime) {
        Job job = new Job();
        job.setTaskId("coupon_invalid_trigger_" + id);
        job.setParam("type", "couponInvalid");
        job.setParam("couponId", id + "");
        job.setTaskTrackerNodeGroup("equity_trade_TaskTracker_" + environment.getActiveProfiles()[0]);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        job.setTriggerTime(triggerTime.getTime());   // 1 小时之后执行
        jobClient.submitJob(job);
    }

    /**
     * 虚拟券失效任务
     * @param id
     */
    public static void virtualTicketsInvalidTrigger(JobClient jobClient, Integer id, Date triggerTime) {
        Job job = new Job();
        job.setTaskId("virtual_invalid_trigger_" + id);
        job.setParam("type", "virtualEnd");
        job.setParam("virtualId", id + "");
        job.setTaskTrackerNodeGroup("equity_trade_TaskTracker_" + environment.getActiveProfiles()[0]);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        job.setTriggerTime(triggerTime.getTime());
        jobClient.submitJob(job);
    }

    /**
     * 用户优惠券释放任务
     * @param id
     */
    public static void couponReleaseTrigger(JobClient jobClient, Integer id) {
        Job job = new Job();
        job.setTaskId("coupon_release_trigger_" + id);
        job.setParam("type", "couponRelease");
        job.setParam("couponUserId", id + "");
        job.setTaskTrackerNodeGroup("equity_trade_TaskTracker_" + environment.getActiveProfiles()[0]);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        job.setTriggerTime(DateUtils.addMinute(new Date(), 30).getTime());   // 半小时之后执行
        jobClient.submitJob(job);
    }
}
