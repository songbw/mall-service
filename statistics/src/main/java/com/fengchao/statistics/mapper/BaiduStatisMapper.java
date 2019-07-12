package com.fengchao.statistics.mapper;

import com.fengchao.statistics.model.BaiduStatis;

public interface BaiduStatisMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BaiduStatis record);

    int insertSelective(BaiduStatis record);

    BaiduStatis selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaiduStatis record);

    int updateByPrimaryKey(BaiduStatis record);
}