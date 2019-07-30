package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.WorkOrdersServiceClient;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderServiceClientH implements WorkOrdersServiceClient {

    @Override
    public OperaResponse refundOrdersCount() {
        return HystrixDefaultFallback.fallbackResponse();
    }
}
