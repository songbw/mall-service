package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.model.AppRatePrice;

import java.util.List;

/**
 * 租户商品类别服务
 */
public interface AppRatePriceService {

    List<AppRatePrice> findListById(int id) ;

    OperaResponse add(AppRatePrice bean);

    OperaResponse update(AppRatePrice bean);

    List<AppRatePrice> findByIds(List<Integer> ids) ;

    void delete(Integer id) ;

}
