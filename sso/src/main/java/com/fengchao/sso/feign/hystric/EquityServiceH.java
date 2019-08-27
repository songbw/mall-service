package com.fengchao.sso.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.sso.feign.EquityService;
import com.fengchao.sso.util.OperaResult;
import org.springframework.stereotype.Component;

@Component
public class EquityServiceH implements EquityService {


    @Override
    public OperaResult findCollectGiftCouponByOpenId(String openId) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString("openId: " + openId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("获取权益服务失败" + msg);
        return result;
    }
}
