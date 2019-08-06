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
    public OperaResult addOrder(OrderParamBean orderBean) {
        OperaResult operaResult = new OperaResult();
        ObjectMapper objectMapper1 = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper1.writeValueAsString(orderBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info(msg);
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
        orderRequest.setInvoiceTitle(orderBean.getInvoiceTitleName());
        orderRequest.setInvoiceContent(orderBean.getInvoiceTitleName());
        List<OrderMerchantBean> orderMerchantBeans = orderBean.getMerchants() ;
        for (OrderMerchantBean orderMerchantBean : orderMerchantBeans) {
            SubOrderT subOrder = new SubOrderT();
            subOrder.setOrderNo(orderMerchantBean.getTradeNo());
            subOrder.setServfee(orderMerchantBean.getServFee() + "");
            subOrder.setAmount(orderMerchantBean.getAmount() + "");
            subOrder.setMerchantNo(orderMerchantBean.getMerchantNo());
            subOrder.setPayment(orderMerchantBean.getPayment());
            subOrder.setOrderType(orderMerchantBean.getType() + "");
            AtomicInteger i= new AtomicInteger(1);
            List<SkusT> aoyiSkus = new ArrayList<>() ;
            orderMerchantBean.getSkus().forEach(sku -> {
                SkusT skus = new SkusT();
                skus.setSkuId(sku.getSkuId());
                skus.setNum(sku.getNum() + "");
                skus.setUnitPrice(sku.getUnitPrice() + "");
                skus.setSubOrderNo(subOrder.getOrderNo() + String.format("%03d", i.getAndIncrement()));
                aoyiSkus.add(skus);
            });
            subOrder.setAoyiSkus(aoyiSkus);
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
        if (r == null) {
            operaResult.setCode(100011);
            operaResult.setMsg("创建订单失败");
            return operaResult;
        }
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
                operaResult.setData(subOrders);
                return operaResult;
            } else {
                operaResult.setCode(100011);
                String message = data.getString("message") ;
                if (message == null || "".equals(message)) {
                    operaResult.setMsg("创建订单失败");
                } else {
                    operaResult.setMsg(message);
                }
                return operaResult;
            }
        } else {
            operaResult.setCode(100011);
            operaResult.setMsg("创建订单失败");
            return operaResult;
        }
    }

    @Override
    public OperaResult confirmOrder(String orderId) {
        OperaResult operaResult = new OperaResult();
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
        if (r == null) {
            operaResult.setCode(100012);
            operaResult.setMsg("确认订单失败");
            return operaResult;
        }
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            operaResult.setData(true);
            return operaResult;
        } else {
            operaResult.setCode(100012);
            String message = r.getString("message") ;
            if (message == null || "".equals(message)) {
                operaResult.setMsg("确认订单失败");
            } else {
                operaResult.setMsg(message);
            }
            return operaResult;
        }
    }

    @Override
    public OperaResult getOrderLogist(QueryLogist queryLogist) {
        OperaResult operaResult = new OperaResult();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(queryLogist);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(queryLogist, JSONObject.class, object, HttpClient.AOYI_ORDER_LOGIST_URL, HttpClient.AOYI_ORDER_LOGIST) ;
        if (r == null) {
            operaResult.setCode(100013);
            operaResult.setMsg("获取物流失败");
            return operaResult;
        }
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            if (data != null) {
                JSONArray data1 = data.getJSONArray("data");
                operaResult.setData(data1);
                return operaResult;
            } else {
                operaResult.setCode(100013);
                operaResult.setMsg("获取物流失败");
                return operaResult;
            }
        } else {
            operaResult.setCode(100013);
            String message = r.getString("message") ;
            if (message == null || "".equals(message)) {
                operaResult.setMsg("获取物流失败");
            } else {
                operaResult.setMsg(message);
            }
            return operaResult;
        }
    }

    @Override
    public OperaResult addOrderGAT(OrderParamBean orderBean) {
        OperaResult operaResult = new OperaResult();
        ObjectMapper objectMapper1 = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper1.writeValueAsString(orderBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info(msg);
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
        orderRequest.setInvoiceTitle(orderBean.getInvoiceTitleName());
        orderRequest.setInvoiceContent(orderBean.getInvoiceTitleName());
        List<OrderMerchantBean> orderMerchantBeans = orderBean.getMerchants() ;
        for (OrderMerchantBean orderMerchantBean : orderMerchantBeans) {
            SubOrderT subOrder = new SubOrderT();
            subOrder.setOrderNo(orderMerchantBean.getTradeNo());
            subOrder.setServfee(orderMerchantBean.getServFee() + "");
            subOrder.setAmount(orderMerchantBean.getAmount() + "");
            subOrder.setMerchantNo(orderMerchantBean.getMerchantNo());
            subOrder.setPayment(orderMerchantBean.getPayment());
            subOrder.setOrderType(orderMerchantBean.getType() + "");
            AtomicInteger i= new AtomicInteger(1);
            List<SkusT> aoyiSkus = new ArrayList<>() ;
            orderMerchantBean.getSkus().forEach(sku -> {
                SkusT skus = new SkusT();
                skus.setSkuId(sku.getSkuId());
                skus.setNum(sku.getNum() + "");
                skus.setUnitPrice(sku.getUnitPrice() + "");
                skus.setSubOrderNo(subOrder.getOrderNo() + String.format("%03d", i.getAndIncrement()));
                aoyiSkus.add(skus);
            });
            subOrder.setAoyiSkus(aoyiSkus);
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
        logger.info("addOrderGAT request is " + jsonString);
        JSONObject r = HttpClient.post(orderRequest, JSONObject.class, object, HttpClient.AOYI_PUSH_ORDER_URL_GAT, HttpClient.AOYI_PUSH_ORDER) ;
        if (r == null) {
            operaResult.setCode(100011);
            operaResult.setMsg("创建订单失败");
            return operaResult;
        }
        try {
            logger.info("addOrderGAT response is " + objectMapper.writeValueAsString(r));
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
                    SubOrderTResponse subOrder = new SubOrderTResponse();
                    JSONObject orderNo = (JSONObject) orderNoResult;
                    subOrder.setOrderNo(orderNo.getString("orderNo"));
                    subOrder.setAoyiId(orderNo.getString("snOrderNo"));
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
                operaResult.setData(subOrders);
                return operaResult;
            } else {
                operaResult.setCode(100011);
                String message = data.getString("message") ;
                if (message == null || "".equals(message)) {
                    operaResult.setMsg("创建订单失败");
                } else {
                    operaResult.setMsg(message);
                }
                return operaResult;
            }
        } else {
            operaResult.setCode(100011);
            operaResult.setMsg("创建订单失败");
            return operaResult;
        }
    }
}
