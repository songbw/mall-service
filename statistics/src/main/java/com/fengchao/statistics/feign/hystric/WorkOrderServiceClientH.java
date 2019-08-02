package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.feign.WorkOrdersServiceClient;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderServiceClientH implements WorkOrdersServiceClient {

    @Setter
    private Throwable cause;

    @Override
    public OperaResponse refundOrdersCount() {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }
}