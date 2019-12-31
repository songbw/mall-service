package com.fengchao.order.feign.hystric;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.BaseServiceClient;
import com.fengchao.order.feign.VendorsServiceClient;
import com.fengchao.order.rpc.extmodel.Email;
import com.fengchao.order.rpc.extmodel.SysCompanyX;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:32
 */
@Component
@Slf4j
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
