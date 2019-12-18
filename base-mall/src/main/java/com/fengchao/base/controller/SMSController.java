package com.fengchao.base.controller;

import com.fengchao.base.bean.OperaResponse;
import com.fengchao.base.bean.SMSPostBean;
import com.fengchao.base.config.SMSConfig;
import com.fengchao.base.utils.JSONUtil;
import com.fengchao.base.utils.SMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class SMSController {

    @PostMapping("/send")
    private OperaResponse send(@RequestBody SMSPostBean bean) {
        OperaResponse<String> response = new OperaResponse<>();
        if (null == bean || null == bean.getPhone()) {
            response.setCode(400);
            response.setMsg("手机号缺失");
            return response;
        }

        String code = SMSUtil.getRandom();
        String[] params = {code, SMSConfig.TENT_ActiveTime};
        try {
            String string = SMSUtil.sendMesModel(params, bean.getPhone(), SMSConfig.TENT_TemplateID1);
        } catch (Exception e) {
            response.setCode(400);
            response.setMsg("发送短信失败 " + e.getMessage());
            return response;
        }

        response.setData(code);
        return response;
    }

    /**
     * 根据模版发送短信
     *
     * @param smsPostBean
     * @return
     */
    @PostMapping("/sendWithTemplate")
    private OperaResponse sendWithTemplate(@RequestBody SMSPostBean smsPostBean) {
        OperaResponse<String> response = new OperaResponse<>();

        try {
            log.info("根据模版发送短信 入参:{}", JSONUtil.toJsonString(smsPostBean));

            // 1. 参数校验
            if (smsPostBean.getPhone() == null) {
                log.warn("根据模版发送短信 缺失手机号");
                throw new Exception("缺失手机号");
            }

            if (smsPostBean.getTemplateId() == null) {
                log.warn("根据模版发送短信 缺失短信模版");
                throw new Exception("缺失短信模版");
            }

            if (smsPostBean.getParams() == null) {
                log.warn("根据模版发送短信 缺失短信参数");
                throw new Exception("缺失短信参数");
            }

            // 2. 发送
            String result = SMSUtil.sendMesModel(smsPostBean.getParams(), smsPostBean.getPhone(), smsPostBean.getTemplateId());

            // 3. 处理结果
            if (!"OK".equalsIgnoreCase(result)) {
                log.warn("根据模版发送短信 返回错误:{}", result);
                throw new Exception(result);
            }

            response.setCode(200);
        } catch (Exception e) {
            log.error("根据模版发送短信 异常:{}", e.getMessage(), e);

            response.setCode(500);
            response.setMsg(e.getMessage());
            return response;
        }

        log.info("根据模版发送短信 返回:{}", JSONUtil.toJsonString(response));
        return response;
    }

}
