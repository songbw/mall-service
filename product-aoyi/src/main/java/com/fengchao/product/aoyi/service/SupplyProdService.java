package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.ProductQueryBean;

/**
 * @author songbw
 * @date 2020/12/28 14:46
 */
public interface SupplyProdService {

    OperaResponse findProductPageable(ProductQueryBean queryBean) ;

    OperaResponse batchFindSkuBySpu(ProductQueryBean queryBean) ;
}
