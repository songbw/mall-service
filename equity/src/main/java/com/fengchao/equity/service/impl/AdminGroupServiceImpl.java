package com.fengchao.equity.service.impl;

import com.alibaba.fastjson.JSON;
import com.fengchao.equity.bean.GroupsBean;
import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.PageableData;
import com.fengchao.equity.bean.vo.GroupInfoReqVo;
import com.fengchao.equity.bean.vo.GroupInfoResVo;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.constants.GroupInfoStatusEnum;
import com.fengchao.equity.constants.LTSConstants;
import com.fengchao.equity.dao.AdminGroupDao;
import com.fengchao.equity.jobClient.LTSJobClient;
import com.fengchao.equity.mapper.GroupsMapper;
import com.fengchao.equity.model.GroupInfo;
import com.fengchao.equity.model.Groups;
import com.fengchao.equity.service.AdminGroupService;
import com.fengchao.equity.utils.ConvertUtil;
import com.fengchao.equity.utils.DateUtil;
import com.fengchao.equity.utils.JSONUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class AdminGroupServiceImpl implements AdminGroupService {

    private GroupsMapper groupsMapper;

    private AdminGroupDao adminGroupDao;

    private LTSJobClient ltsJobClient;

    @Autowired
    public AdminGroupServiceImpl(GroupsMapper groupsMapper,
                                 AdminGroupDao adminGroupDao,
                                 LTSJobClient ltsJobClient) {
        this.groupsMapper = groupsMapper;
        this.adminGroupDao = adminGroupDao;
        this.ltsJobClient = ltsJobClient;
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
        map.put("pageSize", bean.getLimit());
        map.put("name", bean.getName());
        map.put("campaignStatus", bean.getCampaignStatus());
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
        // 1. 插入数据库
        // 转数据库实体GroupInfo
        GroupInfo groupInfo = convertToGroupInfo(groupInfoReqVo);
        groupInfo.setGroupStatus(GroupInfoStatusEnum.CREATED.getValue().shortValue()); // 1:新建 2：未开始(提交/发布) 3：进行中，4：已结束

        log.info("创建拼购活动信息 转GroupInfo:{}", JSONUtil.toJsonString(groupInfoReqVo));
        // 执行插入
        Long id = adminGroupDao.insertGroupInfo(groupInfo);
        log.info("创建拼购活动信息 插入数据库 返回id:{}", id);

        return id;
    }

    @Override
    public Integer publishGroupInfo(GroupInfoReqVo groupInfoReqVo) throws Exception {
        // 1.根据id获取活动信息
        Long groupInfoId = groupInfoReqVo.getId();
        GroupInfo groupInfo = adminGroupDao.selectGroupInfoById(groupInfoId);

        // 2. 校验数据
        String effectiveStartDate = DateUtil.dateTimeFormat(groupInfo.getEffectiveStartDate(),
                DateUtil.DATE_YYYY_MM_DD_HH_MM_SS); // 活动开始时间
        String effectiveEndDate = DateUtil.dateTimeFormat(groupInfo.getEffectiveEndDate(),
                DateUtil.DATE_YYYY_MM_DD_HH_MM_SS); // 活动结束时间
        String nowDate = DateUtil.nowDateTime(DateUtil.DATE_YYYY_MM_DD_HH_MM_SS); // 当前时间
        int price = groupInfo.getGroupBuyingPrice(); // 单位：分

        // 2.1 发布活动，需要提前在活动开始时间点的10分钟前发布
        Long diffMinutes = DateUtil.diffMinutes(nowDate, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS,
                effectiveStartDate, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        if (diffMinutes < 10) {
            throw new Exception("需要提前在活动开始时间点的10分钟前发布");
        }

        // 2.2 活动持续时间需要大于等于12小时
        Long diffHours = DateUtil.diffHours(effectiveStartDate, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS,
                effectiveEndDate, DateUtil.DATE_YYYY_MM_DD_HH_MM_SS);
        if (diffHours < 12) {
            throw new Exception("活动持续时间需要大于等于12小时");
        }

        // 2.3 活动价格校验
        if (price <= 0) {
            throw new Exception("活动价格不合法");
        }

        // 3. 提交任务, 该任务监控活动的开始和结束时间, 当达到开始或结束时间后,翻转活动状态
        // 3.1 提交拼团开始触发任务
        boolean stratTaskResult = ltsJobClient.submitTriggerJob(LTSConstants.GROUP_START_TRIGGER_PREFIX + groupInfoId,
                LTSConstants.GROUP_TYPE_START_TASK, groupInfoReqVo.getEffectiveStartDate().getTime(), groupInfoId + "");

        if (!stratTaskResult) { // TODO :
            log.error("创建 开始触发 拼购任务执行失败！！ startTaskId:{}", LTSConstants.GROUP_START_TRIGGER_PREFIX + groupInfoId);
            throw new Exception("创建 开始触发 拼购任务执行失败");
        }

        // 3.2 提交拼团结束触发任务
        boolean endTaskResult = ltsJobClient.submitTriggerJob(LTSConstants.GROUP_END_TRIGGER_PREFIX + groupInfoId,
                LTSConstants.GROUP_TYPE_END_TASK, groupInfoReqVo.getEffectiveEndDate().getTime(), groupInfoId + "");

        if (!endTaskResult) { // TODO : 这里如果失败，其实需要立即取消该任务，这里不处理的话会有问题，起码应该报警
            log.error("创建 结束触发 拼购任务执行失败！！ endTaskId:{}", LTSConstants.GROUP_END_TRIGGER_PREFIX + groupInfoId);
            throw new Exception("创建 结束触发 拼购任务执行失败");
        }

        // 4. 修改活动状态
        int count = adminGroupDao.updateGroupInfoStatusById(groupInfoId, GroupInfoStatusEnum.UNSTART);

        return count;
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

        // 处理价格 乘100 转成分
        Integer fen = null;
        if (groupInfoReqVo.getGroupBuyingPrice() != null) {
            fen = groupInfoReqVo.getGroupBuyingPrice().multiply(new BigDecimal(100)).intValue();
        }
        groupInfo.setGroupBuyingPrice(fen); //


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

        // 将分转化成元
        BigDecimal yuanPrice = new BigDecimal(0);
        if (groupInfo.getGroupBuyingPrice() != null) { //
            BigDecimal fenPrice = new BigDecimal(groupInfo.getGroupBuyingPrice());
            yuanPrice = fenPrice.divide(new BigDecimal(100));
        }
        groupInfoResVo.setGroupBuyingPrice(yuanPrice);

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
