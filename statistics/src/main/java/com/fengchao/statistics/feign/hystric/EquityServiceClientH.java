package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.EquityServiceClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class EquityServiceClientH implements EquityServiceClient {

    @Setter
    private Throwable cause;

    @Override
    public OperaResponse queryPromotionByIdList(List<Integer> idList) {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }

    @Override
    public OperaResponse queryAllPromotionTypeList() {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }
}

