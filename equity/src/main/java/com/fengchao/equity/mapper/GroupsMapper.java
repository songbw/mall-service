package com.fengchao.equity.mapper;

import com.fengchao.equity.model.Groups;

import java.util.HashMap;
import java.util.List;

public interface GroupsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Groups record);

    int insertSelective(Groups record);

    Groups selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Groups record);

    int updateByPrimaryKey(Groups record);

    int selectCount(HashMap map);

    List<Groups> selectLimit(HashMap map);
}