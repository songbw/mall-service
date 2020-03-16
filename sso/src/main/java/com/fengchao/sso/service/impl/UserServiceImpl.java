package com.fengchao.sso.service.impl;

import com.fengchao.sso.bean.UserBean;
import com.fengchao.sso.dao.UserDao;
import com.fengchao.sso.feign.PinganClientService;
import com.fengchao.sso.model.SUser;
import com.fengchao.sso.util.OperaResult;
import com.github.pagehelper.PageHelper;
import com.fengchao.sso.mapper.UserMapper;
import com.fengchao.sso.mapper.custom.LoginCustomMapper;
import com.fengchao.sso.model.Login;
import com.fengchao.sso.model.User;
import com.fengchao.sso.service.IUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    private PinganClientService pinganClientService;
    @Autowired
    private UserDao userDao;

    public User selectById(Integer id) {
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
        User temp = mapper.selectByOpenId(user);
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
    public User selectUserByOpenId(String openId, String iAppId) {
        User user = new User();
        user.setOpenId(openId);
        user.setiAppId(iAppId);
        return mapper.selectByOpenId(user);
    }

    @Override
    public PageInfo<SUser> selectUser(Integer page, Integer limit, String name, String sex, String telephone, String appId, String openId, String nickName) {
        PageInfo<SUser> users =  userDao.selectUserByPageable(page, limit, name, sex, telephone, appId, openId, nickName);
        return users;
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

    @Override
    public OperaResult findPingAnUser(String userToken) {
        return pinganClientService.findUser(userToken);
    }

    @Override
    public int findUserCount() {
        return mapper.selectCount();
    }

    @Override
    public List<SUser> findByAppIdAndOpenIds(String appId, List<String> openIds) {
        return userDao.selectUserByAppIdAndOpenIds(appId, openIds);
    }

}
