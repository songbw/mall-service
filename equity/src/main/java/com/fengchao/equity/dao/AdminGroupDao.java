package com.fengchao.equity.dao;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.constants.GroupInfoStatusEnum;
import com.fengchao.equity.constants.IStatusEnum;
import com.fengchao.equity.mapper.GroupInfoMapper;
import com.fengchao.equity.mapper.GroupMemberMapper;
import com.fengchao.equity.mapper.GroupTeamMapper;
import com.fengchao.equity.model.*;
import com.fengchao.equity.utils.JSONUtil;
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

    private GroupTeamMapper groupTeamMapper;

    private GroupMemberMapper groupMemberMapper;

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
     * 分页查询活动的team信息列表
     *
     * @param groupInfoId
     * @param pageBean
     * @return
     */
    public PageInfo<GroupTeam> selectPageableGroupTeamListByGroupId(Long groupInfoId, PageBean pageBean) {
        // 设置查询条件
        GroupTeamExample groupTeamExample = new GroupTeamExample();

        groupTeamExample.setOrderByClause("id desc");
        GroupTeamExample.Criteria criteria = groupTeamExample.createCriteria();
        criteria.andIstatusEqualTo(IStatusEnum.VALID.getValue().shortValue());

        criteria.andGroupInfoIdEqualTo(groupInfoId);

        // 分页信息
        PageHelper.startPage(pageBean.getPageNo(), pageBean.getPageSize());
        // 执行查询
        List<GroupTeam> groupTeamList = groupTeamMapper.selectByExample(groupTeamExample);

        log.info("分页查询活动的team信息列表 AdminGroupDao#selectPageableGroupTeamListByGroupId groupInfoId:{}, PageBean:{}, 数据库返回:{}",
                groupInfoId, JSONUtil.toJsonString(pageBean), JSONUtil.toJsonString(groupTeamList));

        // 返回值
        PageInfo<GroupTeam> pageInfo = new PageInfo(groupTeamList);

        return pageInfo;
    }

    /**
     * 根据teamId集合，查询所有的member集合信息
     *
     * @param groupTeamIdList
     * @return
     */
    public List<GroupMember> selectGroupMemberByTeamIds(List<Long> groupTeamIdList) {
        // 组装参数
        GroupMemberExample groupMemberExample = new GroupMemberExample();
        GroupMemberExample.Criteria criteria = groupMemberExample.createCriteria();
        criteria.andIstatusEqualTo(IStatusEnum.INVALID.getValue().shortValue());

        criteria.andGroupTeamIdIn(groupTeamIdList);

        // 执行查询
        List<GroupMember> groupMemberList = groupMemberMapper.selectByExample(groupMemberExample);

        log.info("根据teamId集合，查询所有的member集合信息size:{}", groupMemberList.size());

        return groupMemberList;
    }

    /**
     * 根据id获取GroupInfo
     *
     * @param id
     * @return
     */
    public GroupInfo selectGroupInfoById(Long id) {
        GroupInfo groupInfo = groupInfoMapper.selectByPrimaryKey(id);

        log.info("根据id:{}查询GroupInfo 数据库返回:{}", id, JSONUtil.toJsonString(groupInfo));

        return groupInfo;
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

    /**
     * 根据id更新 groupInfo
     *
     * @param groupInfo
     * @return
     */
    public int updateGroupInfoById(GroupInfo groupInfo) {
        int count = groupInfoMapper.updateByPrimaryKeySelective(groupInfo);

        return count;
    }

    /**
     * 根据groupInfoId更新状态groupInfoStatusEnum
     *
     * @param id
     * @param groupInfoStatusEnum
     * @return
     */
    public int updateGroupInfoStatusById(Long id, GroupInfoStatusEnum groupInfoStatusEnum) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setId(id);
        groupInfo.setGroupStatus(groupInfoStatusEnum.getValue().shortValue());

        int count = groupInfoMapper.updateByPrimaryKeySelective(groupInfo);

        return count;
    }

    /**
     * 更新istatus
     *
     * @param id
     * @param iStatusEnum
     * @return
     */
    public int updateGroupIstatusById(Long id, IStatusEnum iStatusEnum) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setId(id);
        groupInfo.setIstatus(iStatusEnum.getValue().shortValue());

        int count = groupInfoMapper.updateByPrimaryKeySelective(groupInfo);

        return count;
    }


}
