package com.fengchao.sso.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.sso.bean.Order;
import com.fengchao.sso.feign.OrderService;
import com.fengchao.sso.util.OperaResult;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceH implements OrderService {
    @Override
    public OperaResult findOrderListByTradeNo(String tradeNo) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(tradeNo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("获取订单服务失败" + msg);
        return result;
    }

    @Override
    public OperaResult findOrderListByOutTradeNo(String outTradeNo) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(outTradeNo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("获取订单服务失败" + msg);
        return result;
    }

    @Override
    public OperaResult payment(Order order) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("订单服务失败" + msg);
        return result;
    }

    @Override
    public OperaResult findByOutTradeNoAndPaymentNo(String outTradeNo, String paymentNo) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString("outTradeNo: " + outTradeNo + "paymentNo: " + paymentNo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("获取订单服务失败" + msg);
        return result;
    }

    @Override
    public OperaResult paymentByOutTradeNoAndPaymentNo(Order order) {
        OperaResult result = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("订单服务失败" + msg);
        return result;
    }
}
