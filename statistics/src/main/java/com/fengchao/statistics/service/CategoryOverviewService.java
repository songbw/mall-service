package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.vo.CategoryOverviewResVo;

import java.util.List;

public interface CategoryOverviewService {

    /**
     * 每天 按照品类维度统计订单详情总额
     *
     * @param startDateTime
     * @param endDateTime
     */
    void doDailyStatistic(String startDateTime, String endDateTime) throws Exception;

    /**
     * 根据时间范围获取每日统计的数据
     *
     * @param startDate
     * @param endDate
     * @return
     */
    List<CategoryOverviewResVo> fetchStatisticDailyResult(String startDate, String endDate) throws Exception;
}
