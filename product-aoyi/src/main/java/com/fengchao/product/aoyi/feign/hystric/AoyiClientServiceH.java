package com.fengchao.product.aoyi.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.QueryCarriage;
import com.fengchao.product.aoyi.bean.QueryCityPrice;
import com.fengchao.product.aoyi.bean.QueryInventory;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import org.springframework.stereotype.Component;

@Component
public class AoyiClientServiceH implements AoyiClientService {
    @Override
    public OperaResult price(QueryCityPrice queryBean) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(queryBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("价格服务失败 " + msg);
        return result;
    }

    @Override
    public OperaResult inventory(QueryInventory queryBean) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(queryBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("价格服务失败" + msg);
        return result;
    }

    @Override
    public OperaResult shipCarriage(QueryCarriage queryBean) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(queryBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("价格服务失败" + msg);
        return result;
    }
}
