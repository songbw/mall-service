package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.model.MerchantOverview;

import java.util.List;

public interface MerchantOverviewService {

    /**
     * 按商户维度统计订单支付总额
     *
     * @param startDateTime
     * @param endDateTime
     */
    void doDailyStatistic(String startDateTime, String endDateTime);

    List<MerchantOverview> findsum(QueryBean queryBean);
}
