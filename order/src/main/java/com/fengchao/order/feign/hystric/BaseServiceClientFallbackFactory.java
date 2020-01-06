package com.fengchao.order.feign.hystric;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.BaseServiceClient;
import com.fengchao.order.rpc.extmodel.Email;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Author tom
 * @Date 19-7-27 上午10:32
 */
@Component
public class BaseServiceClientFallbackFactory implements FallbackFactory<BaseServiceClient> {

    @Override
    public BaseServiceClient create(Throwable throwable) {
        return new BaseServiceClient() {

            @Override
            public OperaResponse<JSONObject> kuaidi100(String num, String code) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

            @Override
            public OperaResponse sendMail(Email email) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }
        };
    }

}
