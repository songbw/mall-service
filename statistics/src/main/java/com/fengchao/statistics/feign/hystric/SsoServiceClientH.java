package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.SsoServiceClient;
import org.springframework.stereotype.Component;

@Component
public class SsoServiceClientH implements SsoServiceClient {

    @Override
    public OperaResult queryAllUsercount() {
        return HystrixDefaultFallback.defaultFallback();
    }
}
