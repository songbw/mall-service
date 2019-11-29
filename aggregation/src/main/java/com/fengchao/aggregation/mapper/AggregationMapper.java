package com.fengchao.aggregation.mapper;

import com.fengchao.aggregation.model.Aggregation;

import java.util.HashMap;
import java.util.List;

public interface AggregationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Aggregation record);

    int insertSelective(Aggregation record);

    Aggregation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Aggregation record);

    int updateByPrimaryKey(Aggregation record);

    int selectCount(HashMap map);

    List<Aggregation> selectLimit(HashMap map);

    int updateContent(Aggregation record);

    int updateStatus(String appId);

    Aggregation findHomePage(String appId);

    List<Aggregation> selectByGroupId(Integer id);

    Aggregation selectHomePageByAppId(String appId);
}