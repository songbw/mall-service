package com.fengchao.product.aoyi.utils;

import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JobClientUtils {

    @Autowired
    private static Environment environment;
    /**
     * 定时同步商品图片任务
     * @param id
     */
    public static void imageDownUpTrigger(JobClient jobClient, Integer id) {
        Job job = new Job();
        job.setTaskId("image_down_up_trigger_" + id);
        job.setParam("type", "downUp");
        job.setTaskTrackerNodeGroup("product_trade_TaskTracker_" + environment.getActiveProfiles()[0]);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        job.setCronExpression("0 0/5 * * * ?");   // 每5分钟执行1一次
        jobClient.submitJob(job);
    }

    /**
     * 定时同步怡亚通商品和价格任务
     * @param id
     */
    public static void starProductSyncTrigger(JobClient jobClient, Integer id) {
        Job job = new Job();
        job.setTaskId("star_product_trigger_" + id);
        job.setParam("type", "starSync");
        job.setTaskTrackerNodeGroup("product_trade_TaskTracker_" + environment.getActiveProfiles()[0]);
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        job.setCronExpression("0 0/5 * * * ?");   // 每5分钟执行1一次
        jobClient.submitJob(job);
    }
}
