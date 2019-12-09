package com.fengchao.equity.controller;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.QueryProdBean;
import com.fengchao.equity.model.PromotionTags;
import com.fengchao.equity.service.PromotionTagsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/promotionTags", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class PromotionTagsController {


    @Autowired
    private PromotionTagsService service;

    @PostMapping("create")
    public OperaResult createPromotionTags(PromotionTags bean, OperaResult result){
        result.getData().put("result", service.createPromotionTags(bean));
        return result;
    }

    @GetMapping("find")
    public OperaResult findPromotionTags(QueryProdBean bean, OperaResult result){
        result.getData().put("result", service.findPromotionTags(bean.getPageNo(), bean.getPageSize(), bean.getAppId()));
        return result;
    }

    @PutMapping("update")
    public OperaResult updatePromotionTags(PromotionTags bean, OperaResult result){
        result.getData().put("result", service.updatePromotionTags(bean));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deletePromotionTags(Integer id, OperaResult result){
        result.getData().put("result", service.deletePromotionTags(id));
        return result;
    }
}
