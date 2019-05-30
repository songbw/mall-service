package com.fengchao.aoyi.client.service;

import com.fengchao.aoyi.client.bean.*;
import com.fengchao.aoyi.client.exception.AoyiClientException;

import java.util.List;

/**
 * 商品服务
 */
public interface ProductService {

    List<PriceBean> findPrice(QueryCityPrice cityPrice) throws AoyiClientException;

    InventoryBean findInventory(QueryInventory inventory) throws AoyiClientException ;

    FreightFareBean findCarriage(QueryCarriage queryCarriage) throws AoyiClientException ;

    List<CategoryResponse> category() throws AoyiClientException ;

    List<String> getProdSkuPool(Integer categoryId) throws AoyiClientException ;

    List<ProdImage> getProdImage(String skuId) throws AoyiClientException ;

    AoyiProdIndex getProdDetail(String skuId) throws AoyiClientException ;

    String getSaleStatus(String skuId) throws AoyiClientException ;

}
