package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.EquityServiceClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EquityServiceClientH implements EquityServiceClient {

    @Override
    public OperaResponse queryPromotionByIdList(List<Integer> idList) {
        return HystrixDefaultFallback.fallbackResponse();
    }

    @Override
    public OperaResponse queryAllPromotionTypeList() {
        return HystrixDefaultFallback.fallbackResponse();
    }
}

