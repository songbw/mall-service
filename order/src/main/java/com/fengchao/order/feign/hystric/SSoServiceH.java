package com.fengchao.order.feign.hystric;

import com.fengchao.order.bean.BalanceDetail;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.SSoService;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class SSoServiceH implements SSoService {

    @Setter
    private Throwable cause;

    @Override
    public OperaResponse<BalanceDetail> consum(BalanceDetail detail) {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }
}
