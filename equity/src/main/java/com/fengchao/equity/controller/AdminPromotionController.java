package com.fengchao.equity.controller;

import com.fengchao.equity.bean.PromotionBean;
import com.fengchao.equity.model.PromotionX;
import com.fengchao.equity.service.PromotionService;
import com.fengchao.equity.bean.OperaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminPromotion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminPromotionController {

    @Autowired
    private PromotionService service;

    @PostMapping("create")
    public OperaResult createPromotion(@RequestBody PromotionX bean, OperaResult result){
        service.createPromotion(bean);
        result.getData().put("promotionId", bean.getId());
        return result;
    }

    @GetMapping("find")
    public OperaResult findPromotion(Integer offset, Integer limit, OperaResult result){
        result.getData().put("result", service.findPromotion(offset, limit));
        return result;
    }

    @GetMapping("findById")
    public OperaResult findPromotionById(Integer id, OperaResult result){
        result.getData().put("result", service.findPromotionById(id));
        return result;
    }

    @PostMapping("update")
    public OperaResult updatePromotion(@RequestBody PromotionX bean, OperaResult result){
        result.getData().put("result", service.updatePromotion(bean));
        return result;
    }

    @PostMapping("search")
    public OperaResult searchPromotion(@RequestBody PromotionBean bean, OperaResult result){
        result.getData().put("result", service.searchPromotion(bean));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deletePromotion(Integer id, OperaResult result){
        result.getData().put("result", service.deletePromotion(id));
        return result;
    }

    @PostMapping("createContent")
    public OperaResult createContent(@RequestBody PromotionX bean, OperaResult result){
        result.getData().put("result",service.createContent(bean));
        return result;
    }

    @PutMapping("updateContent")
    public OperaResult updateContent(@RequestBody PromotionX bean, OperaResult result){
        result.getData().put("result",service.updateContent(bean));
        return result;
    }

    @DeleteMapping("deleteContent")
    public OperaResult deleteContent(@RequestBody PromotionX bean, OperaResult result){
        result.getData().put("result", service.deleteContent(bean));
        return result;
    }
}
