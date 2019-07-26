package com.fengchao.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.sso.bean.*;
import com.fengchao.sso.feign.AoyiClientService;
import com.fengchao.sso.feign.GuanaitongClientService;
import com.fengchao.sso.feign.OrderService;
import com.fengchao.sso.feign.PinganClientService;
import com.fengchao.sso.service.IPaymentService;
import com.fengchao.sso.util.OperaResult;
import com.fengchao.sso.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private PinganClientService pinganClientService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AoyiClientService aoyiClientService;
    @Autowired
    private GuanaitongClientService guanaitongClientService;

    @Override
    public OperaResult payment(PaymentBean paymentBean) {
        OperaResult result = new OperaResult();
        List<Order> orderList = findTradeNo(paymentBean.getiAppId(), paymentBean.getMerchantNo(),paymentBean.getOpenId() + paymentBean.getOrderNos());
        if ("10".equals(paymentBean.getiAppId())) {
            // 关爱通支付
            GuanaitongPaymentBean guanaitongPaymentBean = new GuanaitongPaymentBean();
            String outer_trade_no = paymentBean.gettAppId() + new Date().getTime() + RandomUtil.getRandomString(3) ;
            guanaitongPaymentBean.setOuter_trade_no(outer_trade_no);
            guanaitongPaymentBean.setBuyer_open_id(paymentBean.getOpenId());
            guanaitongPaymentBean.setReason("下单");
            float total_amount = 0.00f;
            for (Order order: orderList) {
                total_amount = total_amount + order.getSaleAmount() ;
            }
            TradeInfoBean tradeInfoBean = new TradeInfoBean();
            tradeInfoBean.setThirdTradeNo(paymentBean.getiAppId() + paymentBean.getMerchantNo() + paymentBean.getOpenId() + paymentBean.getOrderNos());
            String tradeInfoString = JSON.toJSONString(tradeInfoBean) ;
            guanaitongPaymentBean.setTrade_info(tradeInfoString);
            guanaitongPaymentBean.setReturn_url(paymentBean.getReturnUrl());
            guanaitongPaymentBean.setNotify_url("http://api.weesharing.com/v2/ssoes/payment/back");
            guanaitongPaymentBean.setTotal_amount(total_amount);

            ObjectMapper oMapper = new ObjectMapper();
            Map<String, String> map = oMapper.convertValue(guanaitongPaymentBean, Map.class);
            Result result1 = guanaitongClientService.payment(map) ;
            JSONObject jsonObject = (JSONObject) JSON.toJSON(result1);
            String guanaitongUrl = "https://openapi.guanaitong.cc/seller/pay/excashier?" + jsonObject.getString("data") ;
            orderList.forEach(order -> {
                order.setPaymentNo(outer_trade_no);
                order.setOutTradeNo(paymentBean.getiAppId() + paymentBean.getMerchantNo() + paymentBean.getOpenId() + paymentBean.getOrderNos());
                order.setUpdatedAt(new Date());
                updatePaymentNo(order);
            });
            UrlEncodeBean urlEncodeBean = new UrlEncodeBean();
            urlEncodeBean.setUrlEncode(guanaitongUrl);
            result.getData().put("result", urlEncodeBean);
        } else {
            PaymentResult result1 = getPayment(paymentBean);
            if ("200".equals(result1.getReturnCode())) {
                result.getData().put("result", result1.getData());
                orderList.forEach(order -> {
                    order.setPaymentNo(result1.getData().getOrderNo());
                    order.setOutTradeNo(paymentBean.getiAppId() + paymentBean.getMerchantNo() + paymentBean.getOpenId() + paymentBean.getOrderNos());
                    order.setUpdatedAt(new Date());
                    updatePaymentNo(order);
                });
            } else if ("订单号重复".equals(result1.getMsg())) {
                List<Order> orders = findOutTradeNo(paymentBean.getiAppId() + paymentBean.getMerchantNo() + paymentBean.getOpenId() + paymentBean.getOrderNos());
                if (orders.size() > 0) {
                    OrderNo orderNo = new OrderNo();
                    orderNo.setOrderNo(orders.get(0).getPaymentNo());
                    result.getData().put("result", orderNo);
                } else {
                    result.setCode(-1);
                    result.setMsg(result1.getMsg());
                }
            }else {
                result.setCode(-1);
                result.setMsg(result1.getMsg());
            }
        }
        return result ;
    }

    @Override
    public String back(BackRequest beanRequest) {
        BackBean bean = beanRequest.getData();
        List<Order> orders = findByOutTradeNoAndPaymentNo(bean.getOutTradeNo(), bean.getOrderNo());
        orders.forEach(order1 -> {
            order1.setPaymentAmount(bean.getTotalFee());
            order1.setPaymentAt(new Date());
            order1.setStatus(1);
            order1.setPayee(bean.getPayee());
            order1.setPayer(bean.getPayer());
            order1.setPayStatus(bean.getPayStatus());
            order1.setPaymentTotalFee(bean.getTotalFee());
            order1.setPayOrderCategory(bean.getOrderCategory());
            order1.setPayType(bean.getPayType());
            order1.setRefundFee(bean.getRefundFee());
            updatePaymentByOutTradeNoAndPaymentNo(order1);
            // aoyi确认订单
            confirmOrder(order1.getTradeNo());
        });
        return "success";
    }

    @Override
    public String gNotify(GATBackBean backBean) {
        List<Order> orders = findByPaymentNoAndOpenId(backBean.getOuter_trade_no(), "10" + backBean.getBuyer_open_id());
        int amount = Math.round(backBean.getTotal_amount() * 100);
        orders.forEach(order1 -> {
            order1.setPaymentAmount(amount);
            order1.setPaymentAt(new Date());
            order1.setStatus(1);
            order1.setPayStatus(5);
            updatePaymentByOutTradeNoAndPaymentNo(order1);
            // aoyi确认订单
            confirmOrder(order1.getTradeNo());
        });
        return "success";
    }

    private PaymentResult getPayment(PaymentBean paymentBean) {
        paymentBean.setAppId(paymentBean.getiAppId());
        OperaResult result = pinganClientService.payment(paymentBean);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            PaymentResult paymentResult = JSONObject.parseObject(jsonString, PaymentResult.class) ;
            return paymentResult;
        }
        return null;
    }

    private List<Order> findTradeNo(String appId, String merchantNo, String tradeNo) {
        OperaResult result = orderService.findOrderListByTradeNo(appId, merchantNo, tradeNo);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<Order> orders = JSONObject.parseArray(jsonString, Order.class) ;
            return orders;
        }
        return null;
    }

    private List<Order> findByOutTradeNoAndPaymentNo(String outTradeNo, String paymentNo) {
        OperaResult result = orderService.findByOutTradeNoAndPaymentNo(outTradeNo, paymentNo);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<Order> orders = JSONObject.parseArray(jsonString, Order.class) ;
            return orders;
        }
        return null;
    }

    private List<Order> findByPaymentNoAndOpenId(String paymentNo, String openId) {
        OperaResult result = orderService.findByPaymentNoAndOpenId(paymentNo, openId);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<Order> orders = JSONObject.parseArray(jsonString, Order.class) ;
            return orders;
        }
        return null;
    }

    private List<Order> findOutTradeNo(String outTradeNo) {
        OperaResult result = orderService.findOrderListByOutTradeNo(outTradeNo);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<Order> orders = JSONObject.parseArray(jsonString, Order.class) ;
            return orders;
        }
        return null;
    }

    private void updatePaymentNo(Order order) {
        OperaResult result = orderService.payment(order);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
        }
    }

    private void updatePaymentByOutTradeNoAndPaymentNo(Order order) {
        OperaResult result = orderService.paymentByOutTradeNoAndPaymentNo(order);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
        }
    }

    private boolean confirmOrder(String orderId) {
        OperaResult result = aoyiClientService.conform(orderId);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            return Boolean.parseBoolean(jsonString);
        }
        return false;
    }

}
