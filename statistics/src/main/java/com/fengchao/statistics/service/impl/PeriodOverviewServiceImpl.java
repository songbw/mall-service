package com.fengchao.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.feign.OrderServiceClient;
import com.fengchao.statistics.mapper.PeriodOverviewMapper;
import com.fengchao.statistics.model.PeriodOverview;
import com.fengchao.statistics.service.PeriodOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PeriodOverviewServiceImpl implements PeriodOverviewService {

    @Autowired
    private PeriodOverviewMapper mapper;
    @Autowired
    private OrderServiceClient orderService;

    @Override
    public void add(QueryBean queryBean) {
        PeriodOverview periodOverview = new PeriodOverview();
        int lateAtNight = getPeriodPaymentCount(queryBean.getStartTime() + " 22:00", queryBean.getStartTime() + " 2:00") ;
        int earlyMorning = getPeriodPaymentCount(queryBean.getStartTime() + " 2:00", queryBean.getStartTime() + " 6:00") ;
        int morning = getPeriodPaymentCount(queryBean.getStartTime() + " 6:00", queryBean.getStartTime() + " 10:00") ;
        int noon = getPeriodPaymentCount(queryBean.getStartTime() + " 10:00", queryBean.getStartTime() + " 14:00") ;
        int afternoon = getPeriodPaymentCount(queryBean.getStartTime() + " 14:00", queryBean.getStartTime() + " 18:00") ;
        int night = getPeriodPaymentCount(queryBean.getStartTime() + " 18:00", queryBean.getStartTime() + " 22:00") ;
        periodOverview.setLateAtNight(lateAtNight);
        periodOverview.setEarlyMorning(earlyMorning);
        periodOverview.setMorning(morning);
        periodOverview.setNoon(noon);
        periodOverview.setAfternoon(afternoon);
        periodOverview.setNight(night);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        try {
            Date date = formatter.parse(queryBean.getStartTime()) ;
            periodOverview.setStatisticsDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        periodOverview.setCreatedAt(new Date());
        mapper.insertSelective(periodOverview) ;
    }

    @Override
    public List<PeriodOverview> findByDate(QueryBean queryBean) {
        HashMap map = new HashMap() ;
        map.put("start", queryBean.getStartTime());
        map.put("end", queryBean.getEndTime()) ;
        return mapper.selectByDate(map);
    }

    private int getPeriodPaymentCount(String start, String end) {
        OperaResult result = orderService.paymentCount(start, end);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            Integer count = JSONObject.parseObject(jsonString, Integer.class) ;
            return count;
        }
        return 0;
    }


}
