package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AoyiBaseCategory;

public interface AoyiBaseCategoryMapper {
    int deleteByPrimaryKey(Integer categoryId);

    int insert(AoyiBaseCategory record);

    int insertSelective(AoyiBaseCategory record);

    AoyiBaseCategory selectByPrimaryKey(Integer categoryId);

    int updateByPrimaryKeySelective(AoyiBaseCategory record);

    int updateByPrimaryKey(AoyiBaseCategory record);
}