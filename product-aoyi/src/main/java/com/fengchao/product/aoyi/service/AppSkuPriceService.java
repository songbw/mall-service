package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.model.AppSkuPrice;

import java.util.List;

/**
 * 租户商品类别服务
 */
public interface AppSkuPriceService {

    List<AppSkuPrice> findListById(int id) ;

    OperaResponse add(AppSkuPrice bean);

    OperaResponse update(AppSkuPrice bean);

    List<AppSkuPrice> findByIds(List<Integer> ids) ;

    void delete(Integer id) ;

}
