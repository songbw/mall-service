package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.model.AoyiBaseBrand;

public interface AdminBrandService {

    PageBean findBrandList(Integer offset, Integer limit);

    Integer updateBrandbyId(AoyiBaseBrand bean);

    Integer create(AoyiBaseBrand bean);

    void delete(Integer id);

    PageBean selectNameList(Integer offset, Integer limit, String query);
}
