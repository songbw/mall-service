package com.fengchao.aggregation.service;

import com.fengchao.aggregation.bean.AggregationBean;
import com.fengchao.aggregation.bean.PageBean;
import com.fengchao.aggregation.bean.QueryBean;
import com.fengchao.aggregation.model.Aggregation;

public interface AggregationService {
    PageBean findAggregation(QueryBean bean, Integer merchantId);

    int createAggregation(Aggregation bean);

    Aggregation findAggregationById(Integer id);

    int updateAggregation(Aggregation bean);

    int updateContent(Aggregation bean);

    int deleteAggregation(Integer id);

    PageBean serachAggregation(AggregationBean bean);

    Aggregation findHomePage(String appId);

    Aggregation findAdminAggregationById(Integer id);

    void updateMpuPriceAndStateForAggregation() ;
}
