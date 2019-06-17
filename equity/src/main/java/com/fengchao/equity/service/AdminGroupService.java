package com.fengchao.equity.service;

import com.fengchao.equity.bean.PageBean;
import com.fengchao.equity.bean.GroupsBean;

public interface AdminGroupService {
    int createGroups(GroupsBean bean);

    PageBean findGroups(GroupsBean bean);

    int updateGroups(GroupsBean bean);

    int deleteGroups(Integer id);
}
