package com.fengchao.aoyi.client.service;

import com.fengchao.aoyi.client.bean.*;

import java.util.List;

/**
 * 商品服务
 */
public interface ProductService {

    List<PriceBean> findPrice(QueryCityPrice cityPrice) ;

    InventoryBean findInventory(QueryInventory inventory) ;

    FreightFareBean findCarriage(QueryCarriage queryCarriage) ;

}
