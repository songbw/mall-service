package com.fengchao.equity.service.impl;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.PageableData;
import com.fengchao.equity.bean.vo.*;
import com.fengchao.equity.constants.GroupInfoStatusEnum;
import com.fengchao.equity.constants.IStatusEnum;
import com.fengchao.equity.constants.LTSConstants;
import com.fengchao.equity.dao.AdminGroupDao;
import com.fengchao.equity.jobClient.LTSJobClient;
import com.fengchao.equity.mapper.GroupsMapper;
import com.fengchao.equity.model.GroupInfo;
import com.fengchao.equity.model.GroupMember;
import com.fengchao.equity.model.GroupTeam;
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
import java.util.stream.Collectors;

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
    public GroupInfoResVo queryGroupInfoById(Long id) throws Exception {
        GroupInfo groupInfo = adminGroupDao.selectGroupInfoById(id);

        // 转vo
        GroupInfoResVo groupInfoResVo = convertToGroupInfoResVo(groupInfo);

        log.info("根据id:{}获取活动详情 转GroupInfoResVo:{}", id, JSONUtil.toJsonString(groupInfoResVo));

        return groupInfoResVo;
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
    public Integer updateGroupInfo(GroupInfoReqVo groupInfoReqVo) throws Exception {
        Long groupInfoId = groupInfoReqVo.getId();

        // 根据id从数据库获取groupInfo
        GroupInfo groupInfo = adminGroupDao.selectGroupInfoById(groupInfoId);

        // 判断 groupInfo 状态，只有在'1:新建'状态下，才允许修改
        int groupStatus = groupInfo.getGroupStatus();
        if (groupStatus == GroupInfoStatusEnum.CREATED.getValue()) { // 允许修改
            GroupInfo groupInfoForUpate = convertToGroupInfo(groupInfoReqVo);

            int count = adminGroupDao.updateGroupInfoById(groupInfoForUpate);

            return count;
        } else {
            log.warn("更新拼购活动信息 由于该活动状态不合法，不能更新 GroupInfo:{}", JSONUtil.toJsonString(groupInfo));

            throw new Exception("由于该活动状态不合法，不能更新");
        }
    }


    @Override
    public Integer deleteGroupInfoById(Long id) throws Exception {
        int count = adminGroupDao.updateGroupIstatusById(id, IStatusEnum.INVALID);
        return count;
    }

    @Override
    public PageableData<GroupTeamResVo> queryTeamListPageable(GroupInfoReqVo groupInfoReqVo) throws Exception {
        Long groupId = groupInfoReqVo.getId();

        // 1.分页获取该活动的team
        PageBean pageBean = ConvertUtil.convertToPageBean(groupInfoReqVo);
        PageInfo<GroupTeam> groupTeamPageInfo = adminGroupDao.selectPageableGroupTeamListByGroupId(groupId, pageBean);

        // 2.将获取到的team转vo 和 pageVo
        PageVo pageVo = ConvertUtil.convertToPageVo(groupTeamPageInfo);
        List<GroupTeam> groupTeamList = groupTeamPageInfo.getList();

        // 3.遍历获取到的 groupTeamList, 查询team中的member信息
        // 3.1 获取teamId集合
        List<Long> teamIdList =
                groupTeamList.stream().map(gtl -> gtl.getId()).collect(Collectors.toList());

        // 3.2 根据teamIdList批量查询member集合信息
        List<GroupMember> groupMemberList = adminGroupDao.selectGroupMemberByTeamIds(teamIdList);

        // 4.将查询结果转vo
        // 4.1 将查询到的groupMember集合转vo; 同时形成一个map key：teamId, value : List<GroupMember>
        Map<Long, List<GroupMemberResVo>> groupMemberResVoMap = new HashMap<>();
        for (GroupMember groupMember : groupMemberList) {
            GroupMemberResVo groupMemberResVo = convertToGroupMemberResVo(groupMember); // 转vo

            List<GroupMemberResVo> groupMemberResVoList = groupMemberResVoMap.get(groupMember.getGroupTeamId());
            if (groupMemberResVoList == null) {
                groupMemberResVoList = new ArrayList<>();
            }

            groupMemberResVoList.add(groupMemberResVo);
        }
        log.info("获取活动team详情 组装Map<Long, List<GroupMemberResVo>>为:{}", JSONUtil.toJsonString(groupMemberResVoMap));

        // 4.2 将获取到的groupTeamList转vo，并加入member信息
        List<GroupTeamResVo> groupTeamResVoList = new ArrayList<>();
        for (GroupTeam groupTeam : groupTeamList) {
            GroupTeamResVo groupTeamResVo = convertToGroupTeamResVo(groupTeam); // 转vo

            List<GroupMemberResVo> groupMemberResVoList = groupMemberResVoMap.get(groupTeam.getId()); // 获取该team的成员列表
            groupTeamResVo.setMembers(groupMemberResVoList);

            groupTeamResVoList.add(groupTeamResVo);
        }
        log.info("获取活动team详情 转List<GroupTeamResVo>为:{}", JSONUtil.toJsonString(groupTeamResVoList));


        // 5. 处理返回值
        PageableData<GroupTeamResVo> pageableData = new PageableData<>();
        pageableData.setList(groupTeamResVoList);
        pageableData.setPageInfo(pageVo);

        log.info("获取活动team详情 AdminGroupServiceImpl#queryTeamListPageable 返回:{}", JSONUtil.toJsonString(pageableData));

        return pageableData;
    }


    //======================================= private =======================================

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

    /**
     * 转 GroupMemberResVo
     *
     * @param groupMember
     * @return
     */
    private GroupMemberResVo convertToGroupMemberResVo(GroupMember groupMember) {
        GroupMemberResVo groupMemberResVo = new GroupMemberResVo();

        groupMemberResVo.setId(groupMember.getId());
        groupMemberResVo.setGroupId(groupMember.getGroupInfoId()); // 拼团活动ID
        groupMemberResVo.setGroupTeamId(groupMember.getGroupTeamId()); // 拼团活动teamID
        groupMemberResVo.setMemberOpenId(groupMember.getMemberOpenId()); // 参购人id
        groupMemberResVo.setIsSponser(groupMember.getIsSponser().intValue()); // 是否是团长 1:是 2.否
        groupMemberResVo.setOrderNo(groupMember.getOrderNo()); // 订单id
        groupMemberResVo.setMemberStatus(groupMember.getMemberStatus().intValue()); // 1:预备 2：正式 3：失效 4:退款中
        groupMemberResVo.setIstatus(groupMember.getIstatus().intValue()); // 1:有效 2：无效
        groupMemberResVo.setCreateTime(groupMember.getCreateTime()); // 创建时间
        groupMemberResVo.setUpdateTime(groupMember.getUpdateTime()); // 更新时间

        return groupMemberResVo;
    }

    /**
     * 转 GroupTeamResVo
     *
     * @param groupTeam
     * @return
     */
    private GroupTeamResVo convertToGroupTeamResVo(GroupTeam groupTeam) {
        GroupTeamResVo groupTeamResVo = new GroupTeamResVo();

        groupTeamResVo.setId(groupTeam.getId());
        groupTeamResVo.setGroupId(groupTeam.getGroupInfoId());
        groupTeamResVo.setSponserOpenId(groupTeam.getSponserOpenId()); // 发起人id （团长）
        groupTeamResVo.setMpuId(groupTeam.getMpuId()); // 团购活动商品mpuId
        groupTeamResVo.setTeamStatus(groupTeam.getTeamStatus().intValue()); // team 状态 1：新建 2：进行中(组团中) 3:满员中 4：组团成功，5：组团失效/失败
        groupTeamResVo.setTeamExpiration(groupTeam.getTeamExpiration()); // 成团(team)时效（以“秒”为单位，超过此时间，该team状态值为：组团失败
        groupTeamResVo.setIstatus(groupTeam.getIstatus().intValue()); // 逻辑删除标识 1:有效 2：无效
        groupTeamResVo.setCreateTime(groupTeam.getCreateTime()); // 创建时间
        groupTeamResVo.setUpdateTime(groupTeam.getUpdateTime()); // 更新时间

        groupTeamResVo.setMembers(Collections.EMPTY_LIST); // 成员列表

        return groupTeamResVo;
    }
}
