package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.vo.MerchantOverviewResVo;

import java.util.List;

public interface MerchantOverviewService {

    /**
     * 按商户维度统计订单支付总额
     *
     * @param startDateTime
     * @param endDateTime
     */
    void doDailyStatistic(String startDateTime, String endDateTime);

    /**
     * 根据时间范围获取每日统计的数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    List<MerchantOverviewResVo> fetchStatisticDailyResult(String startDate, String endDate) throws Exception;
}
