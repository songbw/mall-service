package com.fengchao.product.aoyi.utils;

import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import org.springframework.stereotype.Component;

@Component
public class JobClientUtils {

    /**
     * 定时取消订单任务
     * @param id
     */
    public static void imageDownUpTrigger(JobClient jobClient, Integer id) {
        Job job = new Job();
        job.setTaskId("image_down_up_trigger_" + id);
        job.setParam("type", "downUp");
        job.setTaskTrackerNodeGroup("product_trade_TaskTracker");
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        job.setCronExpression("0 0/5 * * * ?");   // 每5分钟执行1一次
        jobClient.submitJob(job);
    }
}
