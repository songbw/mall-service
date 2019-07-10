package com.fengchao.equity.jobClient;

import com.fengchao.equity.utils.JSONUtil;
import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.JobClient;
import com.github.ltsopensource.jobclient.domain.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LTSJobClient {

    private JobClient jobClient;

    /**
     * lts 针对equity的nodeName
     */
    private static final String LTS_NODE_NAME = "equity_trade_TaskTracker";


    @Autowired
    public LTSJobClient(JobClient jobClient) {
        this.jobClient = jobClient;
    }

    public Boolean submitTriggerJob(String taskId, String taskType, Long triggerTime, String params) throws Exception {
        try {
            Job job = new Job();
            job.setTaskId(taskId); // "promotion_effective_trigger_" + id
            job.setParam("type", taskType);
            job.setParam("params", params);
            job.setTaskTrackerNodeGroup(LTS_NODE_NAME);
            job.setNeedFeedback(true);
            job.setReplaceOnExist(true);        // 当任务队列中存在这个任务的时候，是否替换更新
            job.setTriggerTime(triggerTime);   //

            Response response = jobClient.submitJob(job);
            log.info("提交lts任务返回:{}", JSONUtil.toJsonString(response));

            return response.isSuccess();
        } catch (Exception e) {
            log.error("提交任务异常:{}", e.getMessage(), e);

            return false;
        }


    }

}
