package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.model.AoyiBaseCategoryX;

import java.util.List;

/**
 * 商品类别服务
 */
public interface CategoryService {

    List<AoyiBaseCategoryX> findOneLevelList() ;

    List<AoyiBaseCategoryX> findTwoLevelListByOneLevelId(int id) ;

    List<AoyiBaseCategoryX> findListById(int id) ;
}
