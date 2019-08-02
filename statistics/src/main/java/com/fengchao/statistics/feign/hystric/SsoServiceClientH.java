package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.SsoServiceClient;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class SsoServiceClientH implements SsoServiceClient {

    @Setter
    private Throwable cause;

    @Override
    public OperaResult queryAllUsercount() {
        return HystrixDefaultFallback.defaultFallback(cause);
    }
}
