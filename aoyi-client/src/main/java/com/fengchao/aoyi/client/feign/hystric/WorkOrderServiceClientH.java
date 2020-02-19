package com.fengchao.aoyi.client.feign.hystric;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.StarBackBean;
import com.fengchao.aoyi.client.feign.WorkOrderServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WorkOrderServiceClientH implements WorkOrderServiceClient {

    @Override
    public OperaResponse refundStatus(StarBackBean bean) {
        return HystrixDefaultFallback.fallbackResponse(new Throwable());
    }
}

