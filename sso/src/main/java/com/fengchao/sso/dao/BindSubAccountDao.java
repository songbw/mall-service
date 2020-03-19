package com.fengchao.sso.dao;

import com.fengchao.sso.mapper.BindSubAccountMapper;
import com.fengchao.sso.mapper.SUserMapper;
import com.fengchao.sso.model.BindSubAccount;
import com.fengchao.sso.model.BindSubAccountExample;
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
public class BindSubAccountDao {

    private BindSubAccountMapper mapper;

    @Autowired
    public BindSubAccountDao(BindSubAccountMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 根据userId和appId查询用户绑定信息
     *
     * @param appId
     * @param userId
     * @return
     */
    public BindSubAccount selectByUserIdAndAppId(String appId, Integer userId) {
        BindSubAccountExample example  = new BindSubAccountExample();
        BindSubAccountExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId);
        criteria.andUserIdEqualTo(userId);
        List<BindSubAccount> list = mapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0) ;
        }
        return null;
    }

    /**
     * 根据userId和appId查询用户绑定信息
     *
     * @param appId
     * @param openId
     * @return
     */
    public List<BindSubAccount> selectByOpenIdAndAppId(String appId, String openId) {
        BindSubAccountExample example  = new BindSubAccountExample();
        BindSubAccountExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId);
        criteria.andOpenIdEqualTo(openId);
        List<BindSubAccount> list = mapper.selectByExample(example);
        return list;
    }

}
