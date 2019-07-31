package com.fengchao.statistics.service;

import com.fengchao.statistics.bean.vo.PeriodOverviewResVo;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;

import java.util.List;

public interface PeriodOverviewService {

    void doStatistic(List<OrderDetailBean> orderDetailBeanList,
                String startDateTime, String endDateTime) throws Exception;

    /**
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    List<PeriodOverviewResVo> fetchStatisticDailyResult(String startDate, String endDate) throws Exception;
}
