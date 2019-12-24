package com.fengchao.sso.feign.hystric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.bean.Order;
import com.fengchao.sso.feign.OrderServiceClient;
import com.fengchao.sso.util.OperaResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OrderServiceClientFallbackFactory implements FallbackFactory<OrderServiceClient> {

    @Override
    public OrderServiceClient create(Throwable throwable) {
        return new OrderServiceClient() {
            @Override
            public OperaResult findOrderListByTradeNo(String appId, String merchantNo, String tradeNo) {
                OperaResult result = new OperaResult();
                ObjectMapper objectMapper = new ObjectMapper();
                String msg = "";
                try {
                    msg = objectMapper.writeValueAsString(appId + merchantNo + tradeNo);
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
            public OperaResult findByPaymentNoAndOpenId(String paymentNo, String openId) {
                OperaResult result = new OperaResult();
                ObjectMapper objectMapper = new ObjectMapper();
                String msg = "";
                try {
                    msg = objectMapper.writeValueAsString("openId: " + openId + "paymentNo: " + paymentNo);
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

            @Override
            public OperaResponse batchUpdateOrderDetailStatusByOrderIds(List<Integer> orderIdList, Integer status) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

            @Override
            public OperaResult findOrderListByOpenId(String openId) {
                OperaResult result = new OperaResult();
                ObjectMapper objectMapper = new ObjectMapper();
                String msg = "";
                try {
                    msg = objectMapper.writeValueAsString("openId: " + openId);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                result.setCode(404);
                result.setMsg("获取订单服务失败" + msg);
                return result;
            }

            @Override
            public OperaResponse sendTradeInfo(String outTradeNo, String paymentNo) {
                OperaResponse result = new OperaResponse();
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
        };
    }

}

