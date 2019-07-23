package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.model.Overview;

public interface OverviewService {

    void add(QueryBean queryBean) ;

    Overview findSum() ;

}
