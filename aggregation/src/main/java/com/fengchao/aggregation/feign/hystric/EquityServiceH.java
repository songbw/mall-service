package com.fengchao.aggregation.feign.hystric;

import com.fengchao.aggregation.bean.OperaResult;
import com.fengchao.aggregation.feign.EquityService;
import org.springframework.stereotype.Component;

@Component
public class EquityServiceH implements EquityService {

    @Override
    public OperaResult findOnlinePromotion() {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取促销活动信息失败 ");
        return result;
    }
}
