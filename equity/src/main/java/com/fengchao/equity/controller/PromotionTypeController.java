package com.fengchao.equity.controller;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.QueryProdBean;
import com.fengchao.equity.service.PromotionTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/promotion/type", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class PromotionTypeController {

    @Autowired
    private PromotionTypeService service;

    @PostMapping("findPage")
    public OperaResult findPromotionTypesByPage(QueryProdBean params){
        OperaResult result = new OperaResult();
        result.getData().put("result", service.getPromotionTypes(params.getPageNo(), params.getPageSize()));
        return result;
    }


}
