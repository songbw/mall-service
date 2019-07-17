package com.fengchao.statistics.mapper;

import com.fengchao.statistics.model.PromotionOverview;

import java.util.HashMap;
import java.util.List;

public interface PromotionOverviewMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PromotionOverview record);

    int insertSelective(PromotionOverview record);

    PromotionOverview selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PromotionOverview record);

    int updateByPrimaryKey(PromotionOverview record);

    List<PromotionOverview> selectByDate(HashMap map) ;
}