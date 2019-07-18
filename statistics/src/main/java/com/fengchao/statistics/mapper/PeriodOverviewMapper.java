package com.fengchao.statistics.mapper;

import com.fengchao.statistics.model.PeriodOverview;

import java.util.HashMap;
import java.util.List;

public interface PeriodOverviewMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PeriodOverview record);

    int insertSelective(PeriodOverview record);

    PeriodOverview selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PeriodOverview record);

    int updateByPrimaryKey(PeriodOverview record);

    List<PeriodOverview> selectByDate(HashMap map) ;
}