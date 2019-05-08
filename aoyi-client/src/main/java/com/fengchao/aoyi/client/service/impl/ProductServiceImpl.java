package com.fengchao.aoyi.client.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.aoyi.client.bean.*;
import com.fengchao.aoyi.client.service.ProductService;
import com.fengchao.aoyi.client.utils.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public List<PriceBean> findPrice(QueryCityPrice cityPrice) {
        List<PriceBean> priceBeans = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(cityPrice);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(cityPrice, JSONObject.class, object, HttpClient.AOYI_PRODUCT_PRICE_URL, HttpClient.AOYI_PRODUCT_PRICE) ;
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            JSONArray skus = data.getJSONArray("skus");
            skus.forEach(price -> {
                JSONObject p = (JSONObject) price;
                PriceBean priceBean = new PriceBean();
                priceBean.setSkuId(p.getString("skuId"));
                priceBean.setPrice(p.getString("price"));
                priceBeans.add(priceBean);
            });
            return priceBeans;
        } else {
            return null;
        }
    }

    @Override
    public InventoryBean findInventory(QueryInventory inventory) {
        InventoryBean inventoryBean = new InventoryBean();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(inventory);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(inventory, JSONObject.class, object, HttpClient.AOYI_PRODUCT_STOCK_URL, HttpClient.AOYI_PRODUCT_STOCK) ;
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            inventoryBean.setSkuId(data.getString("skuIds"));
            inventoryBean.setState(data.getString("state"));
            inventoryBean.setRemainNum(data.getString("remainNum"));
            return inventoryBean;
        } else {
            return null;
        }
    }

    @Override
    public FreightFareBean findCarriage(QueryCarriage queryCarriage) {
        FreightFareBean freightFareBean = new FreightFareBean();
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(queryCarriage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject r = HttpClient.post(queryCarriage, JSONObject.class, object, HttpClient.AOYI_FREIGHTFARE_URL, HttpClient.AOYI_FREIGHTFARE) ;
        logger.info(r.toJSONString());
        String code = r.getString("CODE");
        if ("0".equals(code)) {
            JSONObject data = r.getJSONObject("RESULT");
            freightFareBean.setFreightFare(data.getString("freightFare"));
            freightFareBean.setMerchantNo(queryCarriage.getMerchantNo());
            freightFareBean.setOrderNo(data.getString("orderNo"));
            return freightFareBean;
        } else {
            return null;
        }
    }
}
