package com.fengchao.order.task;

import com.fengchao.commission.util.FengchaoMailUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author tom
 * @Date 19-9-29 上午11:44
 */
@Slf4j
public class AlarmTask implements Runnable {

    private String title;

    private String content;

    public AlarmTask(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public void run() {
        try {
            log.info("发送邮件告警 开始 title:{}, content:{}", title, content);

            FengchaoMailUtil.send(title, content);

            log.info("发送邮件告警 结束　title:{}, content:{}", title, content);
        } catch (Exception e) {
            log.info("发送邮件告警 异常:{}", e.getMessage(), e);
        }
    }

//    @Override
//    public String toString() {
//        String str = "统一代扣任务 商户单号:" + paymentReqBo.getMerchantNo() + ", step=" + paymentReqBo.getStep() +
//                ", invoker=" + paymentReqBo.getInvoker() + ", retryTimes=" + paymentReqBo.getRetryTimes();
//        return str;
//    }
}
