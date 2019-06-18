package com.fengchao.equity.mapper;

import com.fengchao.equity.model.GroupsBuyer;

public interface GroupsBuyerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupsBuyer record);

    int insertSelective(GroupsBuyer record);

    GroupsBuyer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupsBuyer record);

    int updateByPrimaryKey(GroupsBuyer record);
}