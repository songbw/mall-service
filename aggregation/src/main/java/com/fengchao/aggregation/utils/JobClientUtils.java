package com.fengchao.aggregation.utils;

import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JobClientUtils {

    /**
     * TODO 删除下架mpu任务
     * @param id
     */
    public static void promotionEffectiveTrigger(Environment environment, JobClient jobClient, Integer id, Date triggerTime) {
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


}
