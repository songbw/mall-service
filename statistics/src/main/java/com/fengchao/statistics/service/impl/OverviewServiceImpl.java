package com.fengchao.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.statistics.bean.DayStatisticsBean;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.feign.OrderService;
import com.fengchao.statistics.mapper.OverviewMapper;
import com.fengchao.statistics.model.Overview;
import com.fengchao.statistics.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class OverviewServiceImpl implements OverviewService {

    @Autowired
    private OverviewMapper mapper;
    @Autowired
    private OrderService orderService;

    @Override
    public void add(QueryBean queryBean) {
        Overview overview = new Overview();
        DayStatisticsBean statisticsBean = getStatistics(queryBean) ;
        if (statisticsBean != null) {
            overview.setCreatedAt(new Date());
            overview.setOrderPaymentAmount(statisticsBean.getOrderPaymentAmount());
            overview.setOrderCount(statisticsBean.getOrderCount());
            overview.setOrderPeopleNum(statisticsBean.getOrderPeopleNum());
        }
    }

    private DayStatisticsBean getStatistics(QueryBean queryBean) {
        OperaResult result = orderService.statistics(queryBean.getStartTime(), queryBean.getEndTime());
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            DayStatisticsBean statisticsBean = JSONObject.parseObject(jsonString, DayStatisticsBean.class) ;
            return statisticsBean;
        }
        return null;
    }


}
