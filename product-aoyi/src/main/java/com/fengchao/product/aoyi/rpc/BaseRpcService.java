package com.fengchao.product.aoyi.rpc;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.feign.BaseService;
import com.fengchao.product.aoyi.rpc.extmodel.Email;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseRpcService {

    private BaseService baseServiceClient;

    @Autowired
    public BaseRpcService(BaseService baseServiceClient) {
        this.baseServiceClient = baseServiceClient;
    }

    /**
     * 发送邮件
     *
     * @param receiver
     * @param subject
     * @param content
     * @return
     */
    public void sendMail(String[] receiver, String subject, String content) {
        try {
            Email email = new Email();
            email.setContent(content);
            email.setEmail(receiver);
            email.setSubject(subject);
            log.info("发送邮件 调用base rpc服务 入参:{}", JSONUtil.toJsonString(email));

            OperaResponse operaResponse = baseServiceClient.sendMail(email);
            log.info("发送邮件 调用base rpc服务 返回:{}", JSONUtil.toJsonString(operaResponse));

            // 处理返回
            if (operaResponse.getCode() == 200) {
                log.info("发送邮件 调用base rpc服务 发送成功");
            } else {
                log.info("发送邮件 调用base rpc服务 发送失败:{}", operaResponse.getMsg());
            }
        } catch (Exception e) {
            log.error("发送邮件 异常:{}", e.getMessage(), e);
        }

    }
}
