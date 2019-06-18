package com.fengchao.sso.mapper;

import com.fengchao.sso.bean.UserBean;
import com.fengchao.sso.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    User selectByOpenId(String openId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int updateByUsername(UserBean userBean);
}