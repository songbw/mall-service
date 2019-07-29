package com.fengchao.equity.controller;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.model.PromotionType;
import com.fengchao.equity.service.PromotionTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@RestController
@RequestMapping(value = "/adminPromotion/type", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class PromotionTypeController {

    @Autowired
    private PromotionTypeService service;

    @GetMapping("findPage")
    public OperaResult findPromotionTypesByPage(@QueryParam("pageNo") Integer pageNo, @QueryParam("pageSize") Integer pageSize){
        OperaResult result = new OperaResult();
        result.getData().put("result", service.getPromotionTypes(pageNo, pageSize));
        return result;
    }

    @PostMapping("create")
    public OperaResult createPromotionTypes(@RequestBody PromotionType type){
        OperaResult result = new OperaResult();
        result.getData().put("id", service.createPromotionType(type));
        return result;
    }

    @PutMapping("update")
    public OperaResult updatePromotionTypes(@RequestBody PromotionType type){
        OperaResult result = new OperaResult();
        result.getData().put("id", service.updatePromotionType(type));
        return result;
    }

    @DeleteMapping("remove")
    public OperaResult PromotionTypes(@QueryParam("promotionTypeId") Long promotionTypeId){
        OperaResult result = new OperaResult();
        result.getData().put("result", service.removePromotionType(promotionTypeId));
        return result;
    }


}
