package com.fengchao.order.utils;

import com.github.ltsopensource.core.commons.utils.DateUtils;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JobClientUtils {

    /**
     * 定时取消订单任务
     * @param id
     */
    public static void orderCancelTrigger(JobClient jobClient, Integer id) {
        Job job = new Job();
        job.setTaskId("order_cancel_trigger_" + id);
        job.setParam("type", "orderCancel");
        job.setParam("orderId", id + "");
        job.setTaskTrackerNodeGroup("order_cancel_trade_TaskTracker");
        job.setNeedFeedback(true);
        job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
        job.setTriggerTime(DateUtils.addMinute(new Date(), 30).getTime());   // 1 小时之后执行
        jobClient.submitJob(job);
    }
}
