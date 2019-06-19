package com.fengchao.sso.mapper;

import com.fengchao.sso.model.Login;

public interface LoginMapper {
    int deleteByPrimaryKey(String id);

    int insert(Login record);

    int insertSelective(Login record);

    Login selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Login record);

    int updateByPrimaryKey(Login record);

    int updatePassword(Login login) ;
}