package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.model.AoyiProdIndex;

import java.util.List;

/**
 * 商品服务
 */
public interface ProductService {

    PageBean findList(ProductQueryBean queryBean) ;

    OperaResult findPrice(PriceQueryBean queryBean) ;

    List<InventoryBean> findInventory(InventoryQueryBean queryBean) ;

    List<FreightFareBean> findCarriage(CarriageQueryBean queryBean) ;

    AoyiProdIndex find(String id) ;

    ProductInfoBean findAndPromotion(String skuId);

}
