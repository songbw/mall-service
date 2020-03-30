package com.fengchao.order.feign.hystric;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.FreightsServiceClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author tom
 * @Date 19-7-27 上午10:32
 */
@Component
@Slf4j
public class FreightsServiceClientFallbackFactory implements FallbackFactory<FreightsServiceClient> {

    @Override
    public FreightsServiceClient create(Throwable throwable) {
        return new FreightsServiceClient() {

            @Override
            public OperaResponse queryMerchantExceptionFee(String idList) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }
        };
    }

}
