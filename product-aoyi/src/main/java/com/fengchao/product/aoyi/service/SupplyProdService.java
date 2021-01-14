package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.bean.supply.SupplyBean;

import java.util.List;

/**
 * @author songbw
 * @date 2020/12/28 14:46
 */
public interface SupplyProdService {

    OperaResponse findProductPageable(ProductQueryBean queryBean) ;

    OperaResponse batchFindSkuBySpu(ProductQueryBean queryBean) ;

    OperaResponse batchFindSkuPrice(List<SupplyBean> supplyBeans) ;

    OperaResponse batchFindSkuInventory(List<SupplyBean> supplyBeans) ;
}
