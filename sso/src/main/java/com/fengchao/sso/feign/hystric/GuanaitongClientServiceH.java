package com.fengchao.sso.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.sso.bean.OpenId;
import com.fengchao.sso.bean.PaymentBean;
import com.fengchao.sso.bean.RefundBean;
import com.fengchao.sso.feign.GuanaitongClientService;
import com.fengchao.sso.feign.PinganClientService;
import com.fengchao.sso.util.OperaResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class GuanaitongClientServiceH implements GuanaitongClientService {

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
    public OperaResult findOpenId(OpenId openId) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(openId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("关爱通获取OpenId服务失败" + msg);
        return result;
    }

    @Override
    public OperaResult findUser(OpenId openId) {
        OperaResult result = new OperaResult();
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
}
