package com.fengchao.equity.service.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.GroupsBean;
import com.fengchao.equity.bean.PageableData;
import com.fengchao.equity.bean.vo.GroupInfoReqVo;
import com.fengchao.equity.bean.vo.GroupInfoResVo;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.dao.AdminGroupDao;
import com.fengchao.equity.mapper.GroupInfoMapper;
import com.fengchao.equity.mapper.GroupsMapper;
import com.fengchao.equity.model.GroupInfo;
import com.fengchao.equity.model.Groups;
import com.fengchao.equity.service.AdminGroupService;
import com.fengchao.equity.utils.ConvertUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Convert;
import java.util.*;

@Service
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
        // GroupInfoReqVo 转 GroupInfo
        GroupInfo queryGroupInfo = convertToGroupInfo(groupInfoReqVo);

        // 转PageBean
        PageBean pageBean = ConvertUtil.convertToPageBean(groupInfoReqVo);

        // 执行查询
        PageInfo<GroupInfo> pageInfo = adminGroupDao.queryGroupInfoListPageable(queryGroupInfo, pageBean);

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

        return pageableData;
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
        groupInfo.setSkuId(groupInfoReqVo.getSkuId());
        groupInfo.setEffectiveStartDate(groupInfoReqVo.getEffectiveStartDate());
        groupInfo.setEffectiveEndDate(groupInfoReqVo.getEffectiveEndDate());
        groupInfo.setGroupBuyingPrice(groupInfoReqVo.getGroupBuyingPrice());
        groupInfo.setGroupBuyingQuantity(groupInfoReqVo.getGroupBuyingQuantity());
        groupInfo.setGroupMemberQuantity(groupInfoReqVo.getGroupMemberQuantity());
        groupInfo.setLimitedPerMember(groupInfoReqVo.getLimitedPerMember());
        groupInfo.setDescription(groupInfoReqVo.getDescription());
        groupInfo.setGroupStatus(groupInfoReqVo.getGroupStatus().shortValue());
        groupInfo.setIstatus(groupInfoReqVo.getIstatus().shortValue());
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
        groupInfoResVo.setSkuId(groupInfo.getSkuId());
        groupInfoResVo.setEffectiveStartDate(groupInfo.getEffectiveStartDate());
        groupInfoResVo.setEffectiveEndDate(groupInfo.getEffectiveEndDate());
        groupInfoResVo.setGroupBuyingPrice(groupInfo.getGroupBuyingPrice());
        groupInfoResVo.setGroupBuyingQuantity(groupInfo.getGroupBuyingQuantity());
        groupInfoResVo.setGroupMemberQuantity(groupInfo.getGroupMemberQuantity());
        groupInfoResVo.setLimitedPerMember(groupInfo.getLimitedPerMember());
        groupInfoResVo.setDescription(groupInfo.getDescription());
        groupInfoResVo.setGroupStatus(groupInfo.getGroupStatus().intValue());
        groupInfoResVo.setIstatus(groupInfo.getIstatus().intValue());
        groupInfoResVo.setOperator(groupInfo.getOperator());
        groupInfoResVo.setCreateTime(groupInfo.getCreateTime());
        groupInfoResVo.setUpdateTime(groupInfo.getUpdateTime());

        return groupInfoResVo;
    }
}
