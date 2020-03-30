package com.fengchao.order.feign.hystric;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.FreightsServiceClient;
import com.fengchao.order.rpc.extmodel.ShipTemplateBean;
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
public class FreightsServiceClientFallbackFactory implements FallbackFactory<FreightsServiceClient> {

    @Override
    public FreightsServiceClient create(Throwable throwable) {
        return new FreightsServiceClient() {

            @Override
            public OperaResponse<List<ShipTemplateBean>> queryMerchantExceptionFee(String idList) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }
        };
    }

}
