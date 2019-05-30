package com.fengchao.elasticsearch.feign.hystric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.elasticsearch.domain.OperaResult;
import com.fengchao.elasticsearch.feign.ProductFeign;
import org.springframework.stereotype.Component;

@Component
public class ProductFeignH implements ProductFeign {

    @Override
    public OperaResult getAllEsProductList() {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
//        try {
//            msg = objectMapper.writeValueAsString(queryBean);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        result.setCode(404);
        result.setMsg("商品服务失败 " + msg);
        return result;
    }
}
