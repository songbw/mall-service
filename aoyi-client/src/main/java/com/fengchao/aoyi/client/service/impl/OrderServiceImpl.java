package com.fengchao.aoyi.client.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.aoyi.client.bean.*;
import com.fengchao.aoyi.client.exception.AoyiClientException;
import com.fengchao.aoyi.client.service.OrderService;
import com.fengchao.aoyi.client.utils.HttpClient;
import com.fengchao.aoyi.client.utils.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderServiceImpl implements OrderService {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public List<SubOrderT> addOrder(OrderParamBean orderBean) throws AoyiClientException {
        OrderRequestT orderRequest = new OrderRequestT();
        orderRequest.setTradeNo(orderBean.getTradeNo());
        orderRequest.setCompanyCustNo(orderBean.getCompanyCustNo());
        orderRequest.setReceiverName(orderBean.getReceiverName());
        orderRequest.setTelephone(orderBean.getTelephone());
        orderRequest.setMobile(orderBean.getMobile());
        orderRequest.setEmail(orderBean.getEmail());
        orderRequest.setProvinceId(orderBean.getProvinceId());
        orderRequest.setCityId(orderBean.getCityId());
        orderRequest.setCountyId(orderBean.getCountyId());
        orderRequest.setTownId(orderBean.getTownId());
        orderRequest.setAddress(orderBean.getAddress());
        orderRequest.setZip(orderBean.getZip());
        orderRequest.setInvoiceState(orderBean.getInvoiceState());
        orderRequest.setInvoiceType(orderBean.getInvoiceType());
        orderRequest.setInvoiceTitle(orderBean.getInvoiceTitle());
        orderRequest.setInvoiceContent(orderBean.getInvoiceContent());
        List<OrderMerchantBean> orderMerchantBeans = orderBean.getMerchants() ;
        for (OrderMerchantBean orderMerchantBean : orderMerchantBeans) {
            SubOrderT subOrder = new SubOrderT();
            subOrder.setOrderNo(orderMerchantBean.getTradeNo() + RandomUtil.randomString(orderBean.getTradeNo(), 8));
            subOrder.setServfee(orderMerchantBean.getServFee() + "");
            subOrder.setAmount(orderMerchantBean.getAmount() + "");
            subOrder.setMerchantNo(orderMerchantBean.getMerchantNo());
            subOrder.setPayment(orderMerchantBean.getPayment());
            subOrder.setOrderType(orderMerchantBean.getType() + "");
            AtomicInteger i= new AtomicInteger(1);
            orderMerchantBean.getSkus().forEach(sku -> {
                SkusT skus = new SkusT();
                skus.setSkuId(sku.getSkuId());
                skus.setNum(sku.getNum() + "");
                skus.setUnitPrice(sku.getUnitPrice() + "");
                skus.setSubOrderNo(subOrder.getOrderNo() + String.format("%03d", i.getAndIncrement()));
                subOrder.getAoyiSkus().add(skus);
            });
            orderRequest.getAoyiOrderEntries().add(subOrder);
        }
        String jsonString = JSONObject.toJSONString(orderRequest);
        List<SubOrderT> subOrders = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(orderRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info("addOrder request is " + jsonString);
        JSONObject r = HttpClient.post(orderRequest, JSONObject.class, object, HttpClient.AOYI_PUSH_ORDER_URL, HttpClient.AOYI_PUSH_ORDER) ;
        try {
            logger.info("addOrder response is " + objectMapper.writeValueAsString(r));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            logger.info(data.toJSONString());
            if ("推送成功".equals(data.getString("message"))) {
                JSONArray orderNoResults = data.getJSONArray("orderNoResults");
                orderNoResults.forEach(orderNoResult -> {
                    SubOrderT subOrder = new SubOrderT();
                    JSONObject orderNo = (JSONObject) orderNoResult;
                    subOrder.setOrderNo(orderNo.getString("orderNo"));
                    JSONArray skus = orderNo.getJSONArray("orderSkusResults");
                    List<SkusT> skuses = new ArrayList<>();
                    for (Object o : skus) {
                        JSONObject json = (JSONObject) o;
                        SkusT resSkus = new SkusT();
                        resSkus.setSubOrderNo(json.getString("subOrderNo"));
                        resSkus.setSkuId(json.getString("skuId"));
                        resSkus.setNum(json.getString("num"));
                        resSkus.setUnitPrice(json.getString("unitPrice"));
                        skuses.add(resSkus);
                    }
                    subOrder.setAoyiSkus(skuses);
                    subOrders.add(subOrder);
                });
            } else {
                throw new AoyiClientException(100011, data.getString("message"));
            }
            return subOrders;
        } else {
            throw new AoyiClientException(100012, "创建订单失败");
        }
    }

    @Override
    public boolean confirmOrder(String orderId) throws AoyiClientException {
        OrderConfirmT orderConfirm = new OrderConfirmT();
        orderConfirm.setOrderNo(orderId);
        orderConfirm.setMerchantNo("20");
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(orderConfirm);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(orderConfirm, JSONObject.class, object, HttpClient.AOYI_CONFRIM_ORDER_URL, HttpClient.AOYI_CONFRIM_ORDER) ;
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public JSONArray getOrderLogist(QueryLogist queryLogist) throws AoyiClientException {
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(queryLogist);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(queryLogist, JSONObject.class, object, HttpClient.AOYI_ORDER_LOGIST_URL, HttpClient.AOYI_ORDER_LOGIST) ;
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            if (data != null) {
                JSONArray data1 = data.getJSONArray("data");
                return data1;
            }
            throw new AoyiClientException(100013, "获取物流失败");
        } else {
            throw new AoyiClientException(100013, "获取物流失败");
        }
    }
}
