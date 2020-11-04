package com.fengchao.sso.mapper;

import com.fengchao.sso.bean.UserBean;
import com.fengchao.sso.model.User;

import java.util.HashMap;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    User selectByOpenId(User record);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int updateByUsername(UserBean userBean);

    int selectCount(HashMap map) ;
}