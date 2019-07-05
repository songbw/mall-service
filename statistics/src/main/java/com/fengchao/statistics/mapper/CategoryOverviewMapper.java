package com.fengchao.statistics.mapper;

import com.fengchao.statistics.model.CategoryOverview;

public interface CategoryOverviewMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CategoryOverview record);

    int insertSelective(CategoryOverview record);

    CategoryOverview selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CategoryOverview record);

    int updateByPrimaryKey(CategoryOverview record);
}