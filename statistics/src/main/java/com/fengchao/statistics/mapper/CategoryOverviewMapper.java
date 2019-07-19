package com.fengchao.statistics.mapper;

import com.fengchao.statistics.model.CategoryOverview;
import com.fengchao.statistics.model.MerchantOverview;

import java.util.HashMap;
import java.util.List;

public interface CategoryOverviewMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CategoryOverview record);

    int insertSelective(CategoryOverview record);

    CategoryOverview selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CategoryOverview record);

    int updateByPrimaryKey(CategoryOverview record);

    List<CategoryOverview> selectSum(HashMap map) ;
}