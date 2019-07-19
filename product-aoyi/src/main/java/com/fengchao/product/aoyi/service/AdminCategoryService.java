package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.CategoryBean;
import com.fengchao.product.aoyi.bean.CategoryQueryBean;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryX;

import java.util.List;

public interface AdminCategoryService {

    PageBean selectLimit(Integer offset, Integer limit, Integer categoryClass);

    int insertSelective(AoyiBaseCategoryX bean);

    void delete(Integer id);

    void updateByPrimaryKeySelective(CategoryBean bean);

    PageBean selectNameList(Integer offset, Integer limit, String categoryName);

    List<AoyiBaseCategoryX>  selectCategoryList(Integer id, boolean includeSub);

    List<AoyiBaseCategoryX>  selectAll();

    PageBean selectSubLevelList(Integer offset, Integer limit, Integer parentId);

    List<CategoryQueryBean> selectByCategoryIdList(List<String> categories);
}
