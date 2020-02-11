package com.fengchao.aoyi.client.feign.hystric;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.Orders;
import com.fengchao.aoyi.client.feign.OrderServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderServiceClientH implements OrderServiceClient {

    @Override
    public OperaResponse deliverStatue(Orders order) {
        return HystrixDefaultFallback.fallbackResponse(new Throwable());
    }
}

