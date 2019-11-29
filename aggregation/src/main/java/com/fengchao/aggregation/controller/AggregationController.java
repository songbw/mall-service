package com.fengchao.aggregation.controller;

import com.fengchao.aggregation.bean.OperaResult;
import com.fengchao.aggregation.model.Aggregation;
import com.fengchao.aggregation.service.AggregationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/aggregation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AggregationController {

    @Autowired
    private AggregationService aggregationService;

    @GetMapping("findHomePage")
    public OperaResult findHomePage(String appId, OperaResult result){
        if(StringUtils.isEmpty(appId)){
            result.setCode(500);
            result.setMsg("参数有误");
            return result;
        }
        result.getData().put("result", aggregationService.findHomePage(appId));
        return result;
    }

    @GetMapping("findById")
    public OperaResult findAggregationById(Integer id, OperaResult result){
        Aggregation aggregation = aggregationService.findAggregationById(id);
        result.getData().put("result", aggregation);
        return result;
    }


}
