package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.model.AoyiBaseCategory;

import java.util.List;

/**
 * 商品类别服务
 */
public interface CategoryService {

    List<AoyiBaseCategory> findOneLevelList() ;

    List<AoyiBaseCategory> findTwoLevelListByOneLevelId(int id) ;

    List<AoyiBaseCategory> findListById(int id) ;
}
