package com.fengchao.product.aoyi.service.impl;

import com.fengchao.product.aoyi.mapper.AoyiBaseCategoryMapper;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private AoyiBaseCategoryMapper mapper;

    @Override
    public List<AoyiBaseCategory> findOneLevelList() {
        return mapper.selectOneLevelList();
    }

    @Override
    public List<AoyiBaseCategory> findTwoLevelListByOneLevelId(int id) {
        return mapper.selectListByParentId(id) ;
    }

    @Override
    public List<AoyiBaseCategory> findListById(int id) {
        List<AoyiBaseCategory> categories = mapper.selectListByParentId(id) ;
        categories.forEach(element -> {
            element.setSubs(mapper.selectListByParentId(element.getCategoryId()));
        });
        return categories;
    }
}
