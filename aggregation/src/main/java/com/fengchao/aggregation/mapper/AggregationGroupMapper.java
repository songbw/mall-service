package com.fengchao.aggregation.mapper;

import com.fengchao.aggregation.model.AggregationGroup;

import java.util.HashMap;
import java.util.List;

public interface AggregationGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AggregationGroup record);

    int insertSelective(AggregationGroup record);

    AggregationGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AggregationGroup record);

    int updateByPrimaryKey(AggregationGroup record);

    int selectCount(HashMap map);

    List<AggregationGroup> selectLimit(HashMap map);
}