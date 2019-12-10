package com.fengchao.sso.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.bean.Platform;
import com.fengchao.sso.feign.ProductService;
import org.springframework.stereotype.Component;

/**
 * @author songbw
 * @date 2019/11/26 17:18
 */
@Component
public class ProductServiceH implements ProductService {
    @Override
    public OperaResponse<Platform> findPlatformByAppId(String appId) {
        OperaResponse result = new OperaResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(appId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("根据appId 获取平台服务失败" + msg);
        return result;
    }
}
