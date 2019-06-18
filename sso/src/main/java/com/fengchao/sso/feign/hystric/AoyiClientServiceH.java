package com.fengchao.sso.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.sso.feign.AoyiClientService;
import com.fengchao.sso.util.OperaResult;
import org.springframework.stereotype.Component;

@Component
public class AoyiClientServiceH implements AoyiClientService {

    @Override
    public OperaResult conform(String orderId) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(orderId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("奥义客户端服务失败" + msg);
        return result;
    }
}
