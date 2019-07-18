package com.fengchao.statistics.controller;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.exception.StatisticsException;
import com.fengchao.statistics.mapper.PeriodOverviewMapper;
import com.fengchao.statistics.service.PeriodOverviewService;
import com.fengchao.statistics.service.PromotionOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 时间段概览统计分析
 * @author zp
 */
@RestController
@RequestMapping(value = "/overview/period", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PeriodOverviewController {

    @Autowired
    private PeriodOverviewService service;

    @GetMapping("list")
    private OperaResult overview(String startDate, String endDate, OperaResult result) {
        try{
            QueryBean queryBean = new QueryBean();
            queryBean.setStartTime(startDate);
            queryBean.setEndTime(endDate);
            result.getData().put("result", service.findByDate(queryBean)) ;
            return result;
        }catch (Exception e){
            e.printStackTrace();
            throw new StatisticsException(801001, "请检查参数是否符合要求");
        }
    }

}
