package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.model.PeriodOverview;

import java.util.List;

public interface PeriodOverviewService {

    void add(QueryBean queryBean) ;

    List<PeriodOverview> findByDate(QueryBean queryBean) ;
}
