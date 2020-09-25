package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.model.AppSkuState;

import java.util.List;

/**
 * 租户商品上下架服务
 */
public interface AppSkuStateService {

    List<AppSkuState> findListById(int id) ;

    OperaResponse add(AppSkuState bean);

    OperaResponse addBatch(List<AppSkuState> beans);

    OperaResponse update(AppSkuState bean);

    OperaResponse updateBatchState(List<AppSkuState> beans);

    List<AppSkuState> findByIds(List<Integer> ids) ;

    void delete(Integer id) ;

}
