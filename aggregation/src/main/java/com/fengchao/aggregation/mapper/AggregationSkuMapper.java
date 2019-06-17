package com.fengchao.aggregation.mapper;

import com.fengchao.aggregation.model.AggregationSku;

public interface AggregationSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AggregationSku record);

    int insertSelective(AggregationSku record);

    AggregationSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AggregationSku record);

    int updateByPrimaryKey(AggregationSku record);

    AggregationSku selectBySkuId(String skuid);
}