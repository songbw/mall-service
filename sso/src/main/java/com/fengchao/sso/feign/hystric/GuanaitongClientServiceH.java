package com.fengchao.sso.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.sso.bean.*;
import com.fengchao.sso.feign.GuanaitongClientService;
import com.fengchao.sso.feign.PinganClientService;
import com.fengchao.sso.util.OperaResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Component
public class GuanaitongClientServiceH implements GuanaitongClientService {

    @Override
    public Result payment(Map<String, String> map) {
        Result result = new Result();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("预支付服务失败" + msg);
        return result;
    }

    @Override
    public Result back(RefundBean bean) {
        Result result = new Result();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("退款服务失败" + msg);
        return result;
    }

    @Override
    public Result findOpenId(AuthCode authCode) {
        Result result = new Result();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(authCode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("关爱通获取OpenId服务失败" + msg);
        return result;
    }

    @Override
    public Result findUser(OpenId openId) {
        Result result = new Result();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(openId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("关爱通获取用户服务失败" + msg);
        return result;
    }

    @Override
    public Result refund(Map<String, String> map) {
        Result result = new Result();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("关爱通退款服务失败" + msg);
        return result;
    }
}
