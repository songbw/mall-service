package com.fengchao.sso.mapper;

import com.fengchao.sso.model.Token;

public interface TokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Token record);

    int insertSelective(Token record);

    Token selectByPrimaryKey(Integer id);

    Token selectByOpenId(String openId);

    int updateByPrimaryKeySelective(Token record);

    int updateByPrimaryKey(Token record);
}