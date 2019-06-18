package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.GroupsBean;
import com.fengchao.equity.mapper.GroupsMapper;
import com.fengchao.equity.model.Groups;
import com.fengchao.equity.service.AdminGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class AdminGroupServiceImpl implements AdminGroupService {

    @Autowired
    private GroupsMapper mapper;

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
        return mapper.insertSelective(groups);
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
        total = mapper.selectCount(map);
        if (total > 0) {
            groups = mapper.selectLimit(map);
        }
        pageBean = PageBean.build(pageBean, groups, total, bean.getOffset(), bean.getLimit());
        return pageBean;
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
        return mapper.updateByPrimaryKeySelective(groups);
    }

    @Override
    public int deleteGroups(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }
}
