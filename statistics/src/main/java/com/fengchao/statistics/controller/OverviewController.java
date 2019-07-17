package com.fengchao.statistics.controller;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.bean.QueryUrlStatisBean;
import com.fengchao.statistics.exception.StatisticsException;
import com.fengchao.statistics.service.BaiduStatisService;
import com.fengchao.statistics.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 概览统计分析
 * @author zp
 */
@RestController
@RequestMapping(value = "/overview", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OverviewController {

    @Autowired
    private OverviewService service;

    @GetMapping("sum")
    private OperaResult overview(OperaResult result) {
        try{
            result.getData().put("result", service.findSum()) ;
            return result;
        }catch (Exception e){
            e.printStackTrace();
            throw new StatisticsException(801001, "请检查参数是否符合要求");
        }
    }

}
