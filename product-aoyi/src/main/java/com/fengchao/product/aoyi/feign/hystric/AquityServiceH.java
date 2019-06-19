package com.fengchao.product.aoyi.feign.hystric;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.feign.AquityService;
import org.springframework.stereotype.Component;

@Component
public class AquityServiceH implements AquityService{

    @Override
    public OperaResult find(String skuId) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取促销活动信息失败 " + skuId);
        return result;
    }
}
