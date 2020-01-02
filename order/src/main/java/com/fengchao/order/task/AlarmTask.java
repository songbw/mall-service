package com.fengchao.order.task;

import com.fengchao.order.jobClient.BeanContext;
import com.fengchao.order.rpc.BaseRpcService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @Author tom
 * @Date 19-9-29 上午11:44
 */
@Slf4j
public class AlarmTask extends TraceableBaseTask {

    /**
     * 邮件接收人
     */
    private static String MAIL_ADDRESS = "tom.jing@weesharing.com,bingwei.song@weesharing.com";

    private String title;

    private String content;

    public AlarmTask(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    void execute() {
        // 1. 发送邮件通知相关同学
        BaseRpcService baseRpcService = BeanContext.getBean(BaseRpcService.class);
        baseRpcService.sendMail(MAIL_ADDRESS.split(","),
                BeanContext.getProfile() + "-" + title,
                content + " :: traceId=" + MDC.get("X-B3-TraceId"));
    }
}
