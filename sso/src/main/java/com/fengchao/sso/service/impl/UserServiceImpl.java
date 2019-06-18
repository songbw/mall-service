package com.fengchao.sso.service.impl;

import com.fengchao.sso.bean.UserBean;
import com.github.pagehelper.PageHelper;
import com.fengchao.sso.mapper.UserMapper;
import com.fengchao.sso.mapper.custom.LoginCustomMapper;
import com.fengchao.sso.model.Login;
import com.fengchao.sso.model.User;
import com.fengchao.sso.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private LoginCustomMapper loginCustomMapper;

    public User selectById(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(UserBean bean) {
        User user = new User();
        BeanUtils.copyProperties(bean, user);
        user.setUpdatedAt(new Date());
        return mapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int insertSelective(UserBean bean) {
        User user = new User();
        BeanUtils.copyProperties(bean, user);
        User temp = mapper.selectByOpenId(user.getOpenId());
        if (temp == null) {
            return mapper.insertSelective(user);
        }
        return 0;
    }

    @Override
    public UserBean selectUserByname(Map<String, Object> map) {
        return loginCustomMapper.selectUserByUsesrname(map);
    }

    @Override
    public User selectUserByOpenId(String openId) {
        return mapper.selectByOpenId(openId);
    }

    @Override
    public List<User> selectUser(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        return loginCustomMapper.selectUser();
    }

    @Override
    public int updateByUsername(UserBean userBean) {
        return mapper.updateByUsername(userBean);
    }

    @Override
    public Login selectuserById(UserBean userBean) {
        return loginCustomMapper.selectById(userBean);
    }

    @Override
    public int updateByPrimaryKey(UserBean bean) {
        User user = new User();
        user.setId(bean.getId());
        user.setStatus(bean.getStatus());
        return mapper.updateByPrimaryKey(user);
    }

}
