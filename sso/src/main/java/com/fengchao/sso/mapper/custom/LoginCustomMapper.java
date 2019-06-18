package com.fengchao.sso.mapper.custom;

import com.fengchao.sso.bean.UserBean;
import com.fengchao.sso.model.Login;
import com.fengchao.sso.model.User;

import java.util.List;
import java.util.Map;

public interface LoginCustomMapper {

    void updatePasswordByUsername(Login login);

    UserBean selectUserByUsesrname(Map<String, Object> map);

    List<User> selectUser();

    Login selectById(UserBean userBean);
}