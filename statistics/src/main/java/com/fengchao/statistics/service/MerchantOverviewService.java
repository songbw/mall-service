package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.model.MerchantOverview;

import java.util.List;

public interface MerchantOverviewService {

    /**
     * 按商户统计订单支付总额
     *
     * @param queryBean
     */
    void add(QueryBean queryBean) ;

    List<MerchantOverview> findsum(QueryBean queryBean) ;
}
