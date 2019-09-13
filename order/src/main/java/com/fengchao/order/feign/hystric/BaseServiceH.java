package com.fengchao.order.feign.hystric;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.BaseService;
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
}
