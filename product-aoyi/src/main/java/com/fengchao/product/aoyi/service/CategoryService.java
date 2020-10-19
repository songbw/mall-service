package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.CategoryBean;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryX;
import com.fengchao.product.aoyi.model.AoyiProdIndex;

import java.util.List;

/**
 * 商品类别服务
 */
public interface CategoryService {

    List<AoyiBaseCategoryX> findOneLevelList() ;

    List<AoyiBaseCategoryX> findTwoLevelListByOneLevelId(int id) ;

    List<AoyiBaseCategoryX> findListById(int id) ;

    /**
     * 根据categoryid集合查询
     *
     * @param categoryIdList
     * @return
     */
    List<CategoryBean> queryCategoryListByCategoryIdList(List<Integer> categoryIdList);

    OperaResponse insertOrUpdate(AoyiBaseCategory bean);

    List<AoyiBaseCategory> findByIds(List<Integer> categoryIds) ;

    List<AoyiBaseCategoryX> findOneLevelByAppId(String appId) ;

    List<AoyiBaseCategoryX> findTwoLevelByAppId(String appId, int id) ;

    List<AoyiBaseCategoryX> findListByIdAndAppId(String appId, int id) ;

}
