package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.model.MerchantOverview;

public interface MerchantOverviewService {

    void add(QueryBean queryBean) ;

    MerchantOverview findsum(QueryBean queryBean) ;
}
