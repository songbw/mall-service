package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.model.RenterCategory;

import java.util.List;

/**
 * 租户商品类别服务
 */
public interface RenterCategoryService {

    List<RenterCategory> findListById(int id) ;

    OperaResponse add(RenterCategory bean);

    OperaResponse update(RenterCategory bean);

    List<RenterCategory> findByIds(List<Integer> categoryIds) ;

    void delete(Integer id) ;

}
