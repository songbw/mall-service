package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.mapper.AoyiBaseCategoryXMapper;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryX;
import com.fengchao.product.aoyi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private AoyiBaseCategoryXMapper mapper;

    @Override
    public List<AoyiBaseCategoryX> findOneLevelList() {
        return mapper.selectOneLevelList();
    }

    @Override
    public List<AoyiBaseCategoryX> findTwoLevelListByOneLevelId(int id) {
        return mapper.selectListByParentId(id) ;
    }

    @Override
    public List<AoyiBaseCategoryX> findListById(int id) {
        List<AoyiBaseCategoryX> categories = mapper.selectListByParentId(id) ;
        categories.forEach(element -> {
            element.setSubs(mapper.selectListByParentId(element.getCategoryId()));
        });
        return categories;
    }
}
