package com.fengchao.sso.dao;

import com.fengchao.sso.mapper.SUserMapper;
import com.fengchao.sso.model.SUser;
import com.fengchao.sso.model.SUserExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author song
 * @Date 19-8-19 下午4:54
 */
@Component
@Slf4j
public class UserDao {

    private SUserMapper mapper;

    @Autowired
    public UserDao(SUserMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 分页查询用户列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageInfo<SUser> selectUserByPageable(Integer pageNo, Integer pageSize, String name, String sex, String telephone, String appId, String openId, String nickName, List<String> appIds) {
        SUserExample userExample = new SUserExample();
        SUserExample.Criteria criteria = userExample.createCriteria();
        if (name != null && (!"".equals(name))) {
            criteria.andNameEqualTo(name);
        }
        if (sex != null && (!"".equals(sex))) {
            criteria.andSexEqualTo(sex);
        }
        if (telephone != null && (!"".equals(telephone))) {
            criteria.andTelephoneEqualTo(telephone);
        }
        if (appId != null && (!"".equals(appId))) {
            criteria.andIAppIdEqualTo(appId);
        }
        if (openId != null && (!"".equals(openId))) {
            criteria.andOpenIdLike("%" + openId + "%");
        }
        if (nickName != null && (!"".equals(nickName))) {
            criteria.andNicknameLike("%" + nickName + "%");
        }
        if (appIds != null && appIds.size() > 0) {
            criteria.andIAppIdIn(appIds) ;
        }
        PageHelper.startPage(pageNo, pageSize);
        List<SUser> userList = mapper.selectByExample(userExample);
        PageInfo<SUser> pageInfo = new PageInfo(userList);

        return pageInfo;
    }

    /**
     * 根据手机号和appId查询用户信息
     *
     * @param appId
     * @param telephone
     * @return
     */
    public SUser selectUserByTel(String appId, String telephone) {
        SUserExample userExample = new SUserExample();
        SUserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIAppIdEqualTo(appId);
        criteria.andTelephoneEqualTo(telephone);
        List<SUser> userList = mapper.selectByExample(userExample);
        if (userList != null && userList.size() > 0) {
            return userList.get(0) ;
        }
        return null;
    }

    /**
     * 根据OpenId列表和appId查询用户信息
     * @param appId
     * @param openIds
     * @return
     */
    public List<SUser> selectUserByAppIdAndOpenIds(String appId, List<String> openIds) {
        SUserExample userExample = new SUserExample();
        SUserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIAppIdEqualTo(appId);
        criteria.andOpenIdIn(openIds) ;
        List<SUser> userList = mapper.selectByExample(userExample);
        return userList ;
    }

    /**
     * 根据openId和appId查询用户信息
     *
     * @param appId
     * @param openId
     * @return
     */
    public SUser selectUserByAppIdAndOpenId(String appId, String openId) {
        SUserExample userExample = new SUserExample();
        SUserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIAppIdEqualTo(appId);
        criteria.andOpenIdEqualTo(openId);
        List<SUser> userList = mapper.selectByExample(userExample);
        if (userList != null && userList.size() > 0) {
            return userList.get(0) ;
        }
        return null;
    }
}
