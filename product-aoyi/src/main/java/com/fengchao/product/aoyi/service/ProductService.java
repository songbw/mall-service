package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndex;

import java.util.List;

/**
 * 商品服务
 */
public interface ProductService {

    PageBean findList(ProductQueryBean queryBean) throws ProductException;

    OperaResult findPrice(PriceQueryBean queryBean) throws ProductException ;

    List<InventoryBean> findInventory(InventoryQueryBean queryBean) throws ProductException ;

    List<FreightFareBean> findCarriage(CarriageQueryBean queryBean) throws ProductException ;

    AoyiProdIndex find(String id) throws ProductException ;

    List<AoyiProdIndex> findAll() throws ProductException ;

    ProductInfoBean findAndPromotion(String skuId) throws ProductException;

}
