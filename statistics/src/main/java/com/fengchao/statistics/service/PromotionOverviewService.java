package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.model.PromotionOverview;

import java.util.List;

public interface PromotionOverviewService {

    void add(QueryBean queryBean) ;

    List<PromotionOverview> findByDate(QueryBean queryBean) ;
}
