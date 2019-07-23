package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.model.MerchantOverview;

import java.util.List;

public interface MerchantOverviewService {

    void add(QueryBean queryBean) ;

    List<MerchantOverview> findsum(QueryBean queryBean) ;
}
