package com.fengchao.equity.service;

import com.fengchao.equity.bean.PageableData;
import com.fengchao.equity.bean.vo.GroupInfoReqVo;
import com.fengchao.equity.bean.vo.GroupInfoResVo;
import com.fengchao.equity.bean.vo.GroupTeamResVo;

/**
 * 活动管理服务
 */
public interface AdminGroupService {

    /**
     * 分页获取活动信息列表
     *
     * @param groupInfoReqVo
     * @return
     * @throws Exception
     */
    PageableData<GroupInfoResVo> queryGroupListPageable(GroupInfoReqVo groupInfoReqVo) throws Exception;

    /**
     * 根据id获取活动详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    GroupInfoResVo queryGroupInfoById(Long id) throws Exception;

    /**
     * 创建活动信息
     *
     * @param groupInfoReqVo
     * @return
     * @throws Exception
     */
    Long createGroupInfo(GroupInfoReqVo groupInfoReqVo) throws Exception;

    /**
     * 发布拼购活动
     *
     * @param groupInfoReqVo
     * @return
     * @throws Exception
     */
    Integer publishGroupInfo(GroupInfoReqVo groupInfoReqVo) throws Exception;

    /**
     * 更新groupInfo
     * 只有在‘新建’状态下的记录才可以更新
     *
     * @param groupInfoReqVo
     * @return
     * @throws Exception
     */
    Integer updateGroupInfo(GroupInfoReqVo groupInfoReqVo) throws Exception;

    /**
     * 根据id删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    Integer deleteGroupInfoById(Long id) throws Exception;


    /**
     * 根据groupInfoId分页获取team信息
     *
     * @param groupInfoReqVo
     * @return
     * @throws Exception
     */
    PageableData<GroupTeamResVo> queryTeamListPageable(GroupInfoReqVo groupInfoReqVo) throws Exception;
}
