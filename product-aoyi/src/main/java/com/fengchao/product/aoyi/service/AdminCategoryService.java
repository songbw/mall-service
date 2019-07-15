package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.CategoryBean;
import com.fengchao.product.aoyi.bean.CategoryQueryBean;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;

import java.util.List;

public interface AdminCategoryService {

    PageBean selectLimit(Integer offset, Integer limit, Integer categoryClass);

    int insertSelective(AoyiBaseCategory bean);

    void delete(Integer id);

    void updateByPrimaryKeySelective(CategoryBean bean);

    PageBean selectNameList(Integer offset, Integer limit, String categoryName);

    List<AoyiBaseCategory>  selectCategoryList(Integer id, boolean includeSub);

    List<AoyiBaseCategory>  selectAll();

    PageBean selectSubLevelList(Integer offset, Integer limit, Integer parentId);

    List<CategoryQueryBean> selectByCategoryIdList(List<String> categories);
}
