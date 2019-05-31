package com.fengchao.product.aoyi.mapper;

import com.fengchao.product.aoyi.model.AoyiBaseCategory;

import java.util.List;

public interface AoyiBaseCategoryMapper {
    int deleteByPrimaryKey(Integer categoryId);

    int insert(AoyiBaseCategory record);

    int insertSelective(AoyiBaseCategory record);

    AoyiBaseCategory selectByPrimaryKey(Integer categoryId);

    int updateByPrimaryKeySelective(AoyiBaseCategory record);

    int updateByPrimaryKey(AoyiBaseCategory record);

    List<AoyiBaseCategory> selectOneLevelList();

    List<AoyiBaseCategory> selectListByParentId(Integer parentId);
}