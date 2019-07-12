package com.fengchao.statistics.feign.hystric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.OrderService;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceH implements OrderService {
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
        result.setMsg("获取订单服务失败 " + id);
        return result;
    }
}
