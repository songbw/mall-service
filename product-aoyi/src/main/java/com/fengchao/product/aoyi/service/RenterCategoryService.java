package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.CategoryBean;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.RenterCategoryQueryBean;
import com.fengchao.product.aoyi.model.RenterCategory;

import java.util.List;

/**
 * 租户商品类别服务
 */
public interface RenterCategoryService {

    List<CategoryBean> findListByRenterId(RenterCategoryQueryBean bean) ;

    OperaResponse add(RenterCategory bean);

    OperaResponse addBatch(List<RenterCategory> bean);

    OperaResponse update(RenterCategory bean);

    OperaResponse updateBatch(List<RenterCategory> beans);

    List<RenterCategory> findByIds(List<Integer> categoryIds) ;

    void delete(Integer id) ;

    void deleteBatch(List<Integer> ids) ;

}
