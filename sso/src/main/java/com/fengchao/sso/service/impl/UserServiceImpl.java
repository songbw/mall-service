package com.fengchao.sso.service.impl;

import com.fengchao.sso.bean.UserBean;
import com.fengchao.sso.dao.UserDao;
import com.fengchao.sso.feign.PinganClientService;
import com.fengchao.sso.model.SUser;
import com.fengchao.sso.rpc.VendorsRpcService;
import com.fengchao.sso.util.OperaResult;
import com.fengchao.sso.mapper.UserMapper;
import com.fengchao.sso.mapper.custom.LoginCustomMapper;
import com.fengchao.sso.model.Login;
import com.fengchao.sso.model.User;
import com.fengchao.sso.service.IUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

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
    @Autowired
    private VendorsRpcService vendorsRpcService ;

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
    public PageInfo<SUser> selectUser(String renterHeader, Integer page, Integer limit, String name, String sex, String telephone, String appId, String openId, String nickName) {
        List<String> appIds = new ArrayList<>();
        if (!"0".equals(renterHeader)) {
            appIds = vendorsRpcService.queryAppIdList(renterHeader) ;
            if (appIds == null || appIds.size() <= 0) {
                return new PageInfo<SUser>() ;
            }
        }
        PageInfo<SUser> users =  userDao.selectUserByPageable(page, limit, name, sex, telephone, appId, openId, nickName, appIds);
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
    public int findUserCount(String renterId) {
        List<String> appIds = null;
        if (!StringUtils.isEmpty(renterId) && "0".equals(renterId)) {
            // 查询APPId
            appIds = vendorsRpcService.queryAppIdList(renterId) ;
        }
        HashMap<String, Object> map = new HashMap<>();
        if (appIds != null && appIds.size() > 0) {
            map.put("appIds", appIds);
        }
        return mapper.selectCount(map);
    }

    @Override
    public List<SUser> findByAppIdAndOpenIds(String appId, List<String> openIds) {
        return userDao.selectUserByAppIdAndOpenIds(appId, openIds);
    }

}
