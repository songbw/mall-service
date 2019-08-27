package com.fengchao.equity.feign.hystric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.feign.SSOService;
import org.springframework.stereotype.Component;

@Component
public class SSOServiceH implements SSOService {
    @Override
    public OperaResult findUser(String openId, String iAppId) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        result.setCode(404);
        result.setMsg("获取用户信息失败 " + openId);
        return result;
    }
}
