package com.fengchao.aggregation.service;

import com.fengchao.aggregation.bean.PageBean;
import com.fengchao.aggregation.bean.QueryBean;
import com.fengchao.aggregation.model.AggregationGroup;

public interface AggregationGroupService {
    int createGroup(AggregationGroup bean);

    PageBean findGroup(QueryBean bean, Integer merchantId);

    int updateGroup(AggregationGroup bean);

    int deleteGroup(Integer id);
}
