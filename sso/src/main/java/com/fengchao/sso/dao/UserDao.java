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
    public PageInfo<SUser> selectUserByPageable(Integer pageNo, Integer pageSize) {
        SUserExample userExample = new SUserExample();

        PageHelper.startPage(pageNo, pageSize);
        List<SUser> userList = mapper.selectByExample(userExample);
        PageInfo<SUser> pageInfo = new PageInfo(userList);

        return pageInfo;
    }
}
