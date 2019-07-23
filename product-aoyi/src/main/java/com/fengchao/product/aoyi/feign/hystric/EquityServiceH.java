package com.fengchao.product.aoyi.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.feign.EquityService;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
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

    @Override
    public OperaResult selectCouponBySku(AoyiProdIndexX bean) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("权益服务失败 " + msg);
        return result;
    }
}
