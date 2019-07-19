package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;

import java.util.List;

/**
 * 商品服务
 */
public interface ProductService {

    PageBean findList(ProductQueryBean queryBean) throws ProductException;

    OperaResult findPrice(PriceQueryBean queryBean) throws ProductException ;

    List<InventoryBean> findInventory(InventoryQueryBean queryBean) throws ProductException ;

    List<FreightFareBean> findCarriage(CarriageQueryBean queryBean) throws ProductException ;

    AoyiProdIndexX find(String id) throws ProductException ;

    List<AoyiProdIndexX> findAll() throws ProductException ;

    ProductInfoBean findAndPromotion(String mpu) throws ProductException;

}
