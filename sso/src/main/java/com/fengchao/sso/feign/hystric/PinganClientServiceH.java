package com.fengchao.sso.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.sso.bean.AuthUserBean;
import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.bean.PaymentBean;
import com.fengchao.sso.bean.RefundBean;
import com.fengchao.sso.feign.PinganClientService;
import com.fengchao.sso.util.OperaResult;
import org.springframework.stereotype.Component;

@Component
public class PinganClientServiceH implements PinganClientService {

    @Override
    public OperaResult payment(PaymentBean paymentBean) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(paymentBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("预支付服务失败" + msg);
        return result;
    }

    @Override
    public OperaResult back(RefundBean bean) {
        OperaResult result = new OperaResult();
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
    public OperaResult findToken(String initCode) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(initCode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("获取token服务失败" + msg);
        return result;
    }

    @Override
    public OperaResult findUser(String userToken) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(userToken);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("获取用户服务失败" + msg);
        return result;
    }

    @Override
    public OperaResponse<AuthUserBean> checkRequestCode(String requestCode) {
        OperaResponse result = new OperaResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(requestCode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("根据request code 获取用户服务失败" + msg);
        return result;
    }
}
