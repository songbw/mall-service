package com.fengchao.aggregation.service;

import com.fengchao.aggregation.bean.PageBean;
import com.fengchao.aggregation.model.AggregationGroup;

public interface AggregationGroupService {
    int createGroup(AggregationGroup bean);

    PageBean findGroup(Integer offset, Integer limit,Integer merchantId);

    int updateGroup(AggregationGroup bean);

    int deleteGroup(Integer id);
}
