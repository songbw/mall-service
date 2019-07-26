package com.fengchao.statistics.feign.hystric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.ProductServiceClient;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceH implements ProductServiceClient {
    @Override
    public OperaResult findMerchant(int id) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        result.setCode(404);
        result.setMsg("获取商品商户失败 " + id);
        return result;
    }
}
