package com.fengchao.order.feign.hystric;

import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.EquityService;
import org.springframework.stereotype.Component;

@Component
public class EquityServiceH implements EquityService {

    @Override
    public OperaResult findPromotionBySkuId(String skuId) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取促销活动信息失败 " + skuId);
        return result;
    }
}
