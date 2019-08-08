package com.fengchao.product.aoyi.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import org.springframework.stereotype.Component;

@Component
public class AoyiClientServiceH implements AoyiClientService {
    @Override
    public OperaResponse price(QueryCityPrice queryBean) {
        OperaResponse result = new OperaResponse();
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
    public OperaResponse inventory(QueryInventory queryBean) {
        OperaResponse result = new OperaResponse();
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
    public OperaResponse shipCarriage(QueryCarriage queryBean) {
        OperaResponse result = new OperaResponse();
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
    public OperaResponse priceGAT(QueryCityPrice queryBean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }
}
