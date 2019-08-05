package com.fengchao.aoyi.client.service;

import com.fengchao.aoyi.client.bean.*;
import com.fengchao.aoyi.client.exception.AoyiClientException;

import java.util.List;

/**
 * 商品服务
 */
public interface ProductService {

    OperaResult findPrice(QueryCityPrice cityPrice) ;

    OperaResult findInventory(QueryInventory inventory) ;

    OperaResult findCarriage(QueryCarriage queryCarriage) ;

    OperaResult category() ;

    OperaResult getProdSkuPool(Integer categoryId) ;

    OperaResult getProdImage(String skuId) ;

    OperaResult getProdDetail(String skuId) ;

    OperaResult getSaleStatus(String skuId) ;

    OperaResult findGATPrice(QueryCityPrice cityPrice) ;

}
