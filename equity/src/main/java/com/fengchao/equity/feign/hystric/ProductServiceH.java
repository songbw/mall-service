package com.fengchao.equity.feign.hystric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.feign.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceH implements ProductService {
    @Override
    public OperaResult findCategoryList( List<String> categories) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
//        try {
//            msg = objectMapper.writeValueAsString(queryBean);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        result.setCode(404);
        result.setMsg("获取商品信息失败 " + categories);
        return result;
    }
}
