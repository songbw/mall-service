package com.fengchao.sso.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.bean.SMSPostBean;
import com.fengchao.sso.feign.BaseService;
import org.springframework.stereotype.Component;

/**
 * @author songbw
 * @date 2019/11/27 14:21
 */
@Component
public class BaseServiceH implements BaseService {
    @Override
    public OperaResponse send(SMSPostBean bean) {
        OperaResponse result = new OperaResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("base发送短信服务失败" + msg);
        return result;
    }
}
