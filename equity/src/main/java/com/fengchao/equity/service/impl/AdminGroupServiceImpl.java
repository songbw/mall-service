package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSON;
import com.fengchao.equity.bean.GroupsBean;
import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.PageableData;
import com.fengchao.equity.bean.vo.GroupInfoReqVo;
import com.fengchao.equity.bean.vo.GroupInfoResVo;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.constants.GroupInfoStatusEnum;
import com.fengchao.equity.dao.AdminGroupDao;
import com.fengchao.equity.mapper.GroupsMapper;
import com.fengchao.equity.model.GroupInfo;
import com.fengchao.equity.model.Groups;
import com.fengchao.equity.service.AdminGroupService;
import com.fengchao.equity.utils.ConvertUtil;
import com.fengchao.equity.utils.JSONUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class AdminGroupServiceImpl implements AdminGroupService {

    private GroupsMapper groupsMapper;

    private AdminGroupDao adminGroupDao;

    @Autowired
    public AdminGroupServiceImpl(GroupsMapper groupsMapper,
                                 AdminGroupDao adminGroupDao) {
        this.groupsMapper = groupsMapper;
        this.adminGroupDao = adminGroupDao;
    }

    @Override
    public int createGroups(GroupsBean bean) {
        Groups groups = new Groups();
        groups.setName(bean.getName());
        groups.setCampaignStatus(bean.getCampaignStatus());
        groups.setCreatedTime(new Date());
        groups.setDescription(bean.getDescription());
        groups.setGroupTotal(bean.getGroupTotal());
        groups.setEffectiveEndDate(bean.getEffectiveEndDate());
        groups.setEffectiveStartDate(bean.getEffectiveStartDate());
        groups.setGroupBuyingPrice(bean.getGroupBuyingPrice());
        groups.setGroupBuyingQuantity(bean.getGroupBuyingQuantity());
        groups.setGroupMemberQuantity(bean.getGroupMemberQuantity());
        groups.setImageUrl(bean.getImageUrl());
        groups.setLimitedPerMember(bean.getLimitedPerMember());
        groups.setPaymentExpiration(bean.getPaymentExpiration());
        groups.setSkuid(bean.getSkuid());
        return groupsMapper.insertSelective(groups);
    }

    @Override
    public PageBean findGroups(GroupsBean bean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int pageNo = PageBean.getOffset(bean.getOffset(), bean.getLimit());
        HashMap map = new HashMap();
        map.put("pageNo", pageNo);
        map.put("pageSize",bean.getLimit());
        map.put("name",bean.getName());
        map.put("campaignStatus",bean.getCampaignStatus());
        List<Groups> groups = new ArrayList<>();
        total = groupsMapper.selectCount(map);
        if (total > 0) {
            groups = groupsMapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, groups, total, bean.getOffset(), bean.getLimit());
        return pageBean;
    }

    @Override
    public PageableData<GroupInfoResVo> queryGroupListPageable(GroupInfoReqVo groupInfoReqVo) throws Exception {
        // 返回值
        PageableData<GroupInfoResVo> pageableData = new PageableData<>();

        // 1.查询逻辑
        // 组装查询参数(GroupInfoReqVo 转 GroupInfo)
        GroupInfo queryGroupInfo = convertToGroupInfo(groupInfoReqVo);

        // 转PageBean
        PageBean pageBean = ConvertUtil.convertToPageBean(groupInfoReqVo);

        log.info("分页查询活动列表 组装查询参数 GroupInfo:{}, PageBean:{}",
                JSONUtil.toJsonString(queryGroupInfo), JSONUtil.toJsonString(pageBean));

        // 执行查询
        PageInfo<GroupInfo> pageInfo = adminGroupDao.selectGroupInfoListPageable(queryGroupInfo, pageBean);

        // 2.处理结果
        PageVo pageVo = ConvertUtil.convertToPageVo(pageInfo);
        List<GroupInfo> groupInfoList = pageInfo.getList();

        // 转GroupInfoResVo集合
        List<GroupInfoResVo> groupInfoResVoList = new ArrayList<>();
        for (GroupInfo groupInfo : groupInfoList) {
            GroupInfoResVo groupInfoResVo = convertToGroupInfoResVo(groupInfo);

            groupInfoResVoList.add(groupInfoResVo);
        }

        //
        pageableData.setList(groupInfoResVoList);
        pageableData.setPageInfo(pageVo);

        log.info("分页查询活动列表 得到PageableData为:{}", JSONUtil.toJsonString(pageableData));

        return pageableData;
    }

    @Override
    public Long createGroupInfo(GroupInfoReqVo groupInfoReqVo) throws Exception {
        // 1. 校验数据
        // 1.1 活动开始时间需要晚于当前时间30分钟


        // 2. 提交任务, 该任务监控活动的开始和结束时间, 当达到开始或结束时间后,翻转活动状态



        // 3. 插入数据库
        // 转数据库实体GroupInfo
        GroupInfo groupInfo = convertToGroupInfo(groupInfoReqVo);
        groupInfo.setGroupStatus(GroupInfoStatusEnum.UNSTART.getValue().shortValue()); // 活动状态（1：未开始，2：进行中，3：已结束）

        log.info("创建活动信息 转GroupInfo:{}", JSONUtil.toJsonString(groupInfoReqVo));
        // 执行插入
        Long id = adminGroupDao.insertGroupInfo(groupInfo);

        log.info("创建活动信息 插入数据库 返回id:{}", id);
        return id;
    }

    @Override
    public int updateGroups(GroupsBean bean) {
        Groups groups = new Groups();
        groups.setId(bean.getId());
        groups.setName(bean.getName());
        groups.setCampaignStatus(bean.getCampaignStatus());
        groups.setDescription(bean.getDescription());
        groups.setGroupTotal(bean.getGroupTotal());
        groups.setEffectiveEndDate(bean.getEffectiveEndDate());
        groups.setEffectiveStartDate(bean.getEffectiveStartDate());
        groups.setGroupBuyingPrice(bean.getGroupBuyingPrice());
        groups.setGroupBuyingQuantity(bean.getGroupBuyingQuantity());
        groups.setGroupMemberQuantity(bean.getGroupMemberQuantity());
        groups.setImageUrl(bean.getImageUrl());
        groups.setLimitedPerMember(bean.getLimitedPerMember());
        groups.setPaymentExpiration(bean.getPaymentExpiration());
        groups.setSkuid(bean.getSkuid());
        return groupsMapper.updateByPrimaryKeySelective(groups);
    }

    @Override
    public int deleteGroups(Integer id) {
        return groupsMapper.deleteByPrimaryKey(id);
    }


    /**
     * GroupInfoReqVo 转 GroupInfo
     *
     * @param groupInfoReqVo
     * @return
     */
    private GroupInfo convertToGroupInfo(GroupInfoReqVo groupInfoReqVo) {
        GroupInfo groupInfo = new GroupInfo();

        groupInfo.setId(groupInfoReqVo.getId());
        groupInfo.setName(groupInfoReqVo.getName());
        groupInfo.setMpuId(groupInfoReqVo.getMpuId());
        groupInfo.setEffectiveStartDate(groupInfoReqVo.getEffectiveStartDate());
        groupInfo.setEffectiveEndDate(groupInfoReqVo.getEffectiveEndDate());
        groupInfo.setGroupBuyingPrice(groupInfoReqVo.getGroupBuyingPrice());
        groupInfo.setGroupBuyingQuantity(groupInfoReqVo.getGroupBuyingQuantity());
        groupInfo.setGroupMemberQuantity(groupInfoReqVo.getGroupMemberQuantity());
        groupInfo.setLimitedPerMember(groupInfoReqVo.getLimitedPerMember());
        groupInfo.setDescription(groupInfoReqVo.getDescription());
        groupInfo.setGroupStatus(groupInfoReqVo.getGroupStatus() == null ? null : groupInfoReqVo.getGroupStatus().shortValue());
        groupInfo.setIstatus(groupInfoReqVo.getIstatus() == null ? null : groupInfoReqVo.getIstatus().shortValue());
        groupInfo.setOperator(groupInfoReqVo.getOperator());

        return groupInfo;
    }

    /**
     * GroupInfo 转 GroupInfoResVo
     *
     * @param groupInfo
     * @return
     */
    private GroupInfoResVo convertToGroupInfoResVo(GroupInfo groupInfo) {
        GroupInfoResVo groupInfoResVo = new GroupInfoResVo();
        groupInfoResVo.setId(groupInfo.getId());
        groupInfoResVo.setName(groupInfo.getName());
        groupInfoResVo.setMpuId(groupInfo.getMpuId());
        groupInfoResVo.setEffectiveStartDate(groupInfo.getEffectiveStartDate());
        groupInfoResVo.setEffectiveEndDate(groupInfo.getEffectiveEndDate());
        groupInfoResVo.setGroupBuyingPrice(groupInfo.getGroupBuyingPrice());
        groupInfoResVo.setGroupBuyingQuantity(groupInfo.getGroupBuyingQuantity());
        groupInfoResVo.setGroupMemberQuantity(groupInfo.getGroupMemberQuantity());
        groupInfoResVo.setLimitedPerMember(groupInfo.getLimitedPerMember());
        groupInfoResVo.setDescription(groupInfo.getDescription());
        groupInfoResVo.setGroupStatus(groupInfo.getGroupStatus() == null ? null : groupInfo.getGroupStatus().intValue());
        groupInfoResVo.setIstatus(groupInfo.getIstatus() == null ? null : groupInfo.getIstatus().intValue());
        groupInfoResVo.setOperator(groupInfo.getOperator());
        groupInfoResVo.setCreateTime(groupInfo.getCreateTime());
        groupInfoResVo.setUpdateTime(groupInfo.getUpdateTime());

        return groupInfoResVo;
    }
}
