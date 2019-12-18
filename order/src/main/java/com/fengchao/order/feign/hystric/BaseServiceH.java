package com.fengchao.order.feign.hystric;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.BaseService;
import com.fengchao.order.rpc.extmodel.Email;
import com.fengchao.order.rpc.extmodel.SMSPostBean;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class BaseServiceH implements BaseService {

    @Setter
    private Throwable cause;

    @Override
    public OperaResponse<JSONObject> kuaidi100(String num, String code) {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }

    @Override
    public OperaResponse<String> sendWithTemplate(SMSPostBean smsPostBean) {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }

    @Override
    public OperaResponse sendMail(Email email) {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }
}
