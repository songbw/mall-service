package com.fengchao.sso.service;

import com.fengchao.sso.bean.UserBean;
import com.fengchao.sso.model.Login;
import com.fengchao.sso.model.SUser;
import com.fengchao.sso.model.User;
import com.fengchao.sso.util.OperaResult;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface IUserService {
    User selectById(String id);

    int update(UserBean bean);

    int insertSelective(UserBean bean);

    UserBean selectUserByname(Map<String,Object> map);

    User selectUserByOpenId(String openId, String iAppId);

    PageInfo<SUser> selectUser(Integer page, Integer limit);

    int updateByUsername(UserBean userBean);

    Login selectuserById(UserBean userBean);

    int updateByPrimaryKey(UserBean userBean);

    OperaResult findPingAnUser(String userToken) ;

    int findUserCount() ;
}
