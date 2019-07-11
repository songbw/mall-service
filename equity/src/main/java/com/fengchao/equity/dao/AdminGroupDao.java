package com.fengchao.equity.dao;

import com.alibaba.fastjson.JSON;
import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.constants.IStatusEnum;
import com.fengchao.equity.mapper.GroupInfoMapper;
import com.fengchao.equity.model.GroupInfo;
import com.fengchao.equity.model.GroupInfoExample;
import com.fengchao.equity.utils.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AdminGroupDao {

    private GroupInfoMapper groupInfoMapper;

    @Autowired
    public AdminGroupDao(GroupInfoMapper groupInfoMapper) {
        this.groupInfoMapper = groupInfoMapper;
    }

    /**
     * 分页查询活动信息列表
     *
     * @param groupInfo
     * @param pageBean
     * @return
     */
    public PageInfo<GroupInfo> selectGroupInfoListPageable(GroupInfo groupInfo, PageBean pageBean) {
        // 设置查询条件
        GroupInfoExample groupInfoExample = new GroupInfoExample();

        groupInfoExample.setOrderByClause("id desc");
        GroupInfoExample.Criteria criteria = groupInfoExample.createCriteria();

        criteria.andIstatusEqualTo(IStatusEnum.VALID.getValue().shortValue());

        if (StringUtils.isNotBlank(groupInfo.getName())) {
            criteria.andNameLike(groupInfo.getName());
        }

        if (groupInfo.getGroupStatus() != null && groupInfo.getGroupStatus() > 0) {
            criteria.andGroupStatusEqualTo(groupInfo.getGroupStatus());
        }

        // 分页信息
        PageHelper.startPage(pageBean.getPageNo(), pageBean.getPageSize());
        // 执行查询
        List<GroupInfo> groupInfoList = groupInfoMapper.selectByExample(groupInfoExample);

        log.info("分页查询活动列表 AdminGroupDao#queryGroupInfoListPageable 数据库返回:{}", JSONUtil.toJsonString(groupInfoList));

        // 返回值
        PageInfo<GroupInfo> pageInfo = new PageInfo(groupInfoList);

        return pageInfo;
    }

    /**
     * 插入活动信息
     *
     * @param groupInfo
     * @return
     */
    public Long insertGroupInfo(GroupInfo groupInfo) {
        // 插入
        int count = groupInfoMapper.insertSelective(groupInfo);

        return groupInfo.getId();
    }
}
