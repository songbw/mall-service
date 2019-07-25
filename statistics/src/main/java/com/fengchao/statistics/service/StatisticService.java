package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.QueryBean;

/**
 * 统计服务
 *
 * @Author tom
 * @Date 19-7-25 下午3:23
 */
public interface StatisticService {

    void doStatistic(String startDateTime, String endDateTime);
}
