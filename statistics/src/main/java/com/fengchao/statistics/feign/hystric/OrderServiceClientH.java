package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.OrderServiceClient;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceClientH implements OrderServiceClient {

    @Override
    public OperaResult statistics(String start, String end) {
        return HystrixDefaultFallback.defaultFallback();
    }

    @Override
    public OperaResult paymentCount(String start, String end) {
        return HystrixDefaultFallback.defaultFallback();
    }

    @Override
    public OperaResult paymentPromotionCount(String start, String end) {
        return HystrixDefaultFallback.defaultFallback();
    }

    @Override
    public OperaResult paymentMerchantCount(String start, String end) {
        return HystrixDefaultFallback.defaultFallback();
    }

    @Override
    public OperaResult queryPayedOrderDetailList(String start, String end) {
        return HystrixDefaultFallback.defaultFallback();
    }
}
