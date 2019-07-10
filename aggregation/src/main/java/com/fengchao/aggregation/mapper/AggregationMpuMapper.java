package com.fengchao.aggregation.mapper;

import com.fengchao.aggregation.model.AggregationMpu;

public interface AggregationMpuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AggregationMpu record);

    int insertSelective(AggregationMpu record);

    AggregationMpu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AggregationMpu record);

    int updateByPrimaryKey(AggregationMpu record);

    AggregationMpu selectByMpu(String skuid);
}