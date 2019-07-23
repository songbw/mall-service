package com.fengchao.order.feign.hystric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceH implements ProductService {
    @Override
    public OperaResult find(String id) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
//        try {
//            msg = objectMapper.writeValueAsString(queryBean);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        result.setCode(404);
        result.setMsg("获取商品信息失败 " + id);
        return result;
    }

    @Override
    public OperaResult findProductListByMpuIdList(List<String> mpuIdList) {
        return HystrixDefaultFallback.defaultFallback();
    }
}
