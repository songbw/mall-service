package com.fengchao.equity.service;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.GroupsBean;
import com.fengchao.equity.bean.PageableData;
import com.fengchao.equity.bean.vo.GroupInfoReqVo;
import com.fengchao.equity.bean.vo.GroupInfoResVo;

/**
 * 活动管理服务
 */
public interface AdminGroupService {

    int createGroups(GroupsBean bean);

    PageBean findGroups(GroupsBean bean);

    int updateGroups(GroupsBean bean);

    int deleteGroups(Integer id);


    /**
     * 分页获取活动信息列表
     *
     * @param groupInfoReqVo
     * @return
     * @throws Exception
     */
    PageableData<GroupInfoResVo> queryGroupListPageable(GroupInfoReqVo groupInfoReqVo) throws Exception;


}
