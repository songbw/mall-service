package com.fengchao.base.mapper;

import com.fengchao.base.model.Tags;

import java.util.List;

public interface TagsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tags record);

    int insertSelective(Tags record);

    Tags selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tags record);

    int updateByPrimaryKey(Tags record);

    List<Tags> selectByAppId(String appId);
}