package com.fengchao.base.controller;

import com.fengchao.base.bean.Email;
import com.fengchao.base.bean.OperaResponse;
import com.fengchao.base.bean.SMSPostBean;
import com.fengchao.base.config.SMSConfig;
import com.fengchao.base.service.MailService;
import com.fengchao.base.utils.SMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SMSController {

    @PostMapping("/send")
    private OperaResponse send(@RequestBody SMSPostBean bean) {
        OperaResponse<String> response = new OperaResponse<>();
        if (null == bean || null == bean.getPhone()){
            response.setCode(400);
            response.setMsg("手机号缺失");
            return response;
        }

        String code = SMSUtil.getRandom();
        String[] params = {code, SMSConfig.TENT_ActiveTime};
        try {
            String string = SMSUtil.sendMesModel(params, bean.getPhone(), SMSConfig.TENT_TemplateID1);
        }catch (Exception e){
            response.setCode(400);
            response.setMsg("发送短信失败 "+e.getMessage());
            return response;
        }

        response.setData(code);
        return response ;
    }

}
