package com.fengchao.equity.controller;

import com.fengchao.equity.service.PromotionService;
import com.fengchao.equity.bean.OperaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/promotion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PromotionController {

    @Autowired
    private PromotionService service;

    @GetMapping("findPromotion")
    public OperaResult findPromotionToUser(Integer id, Boolean detail, OperaResult result){
        result.getData().put("result", service.findPromotionToUser(id, detail));
        return result;
    }

    @GetMapping("mpu")
    public OperaResult findPromotionByMpu(@RequestParam("skuId")String Mpu, OperaResult result){
        result.getData().put("result", service.findPromotionByMpu(Mpu));
        return result;
    }

    @GetMapping("findById")
    public OperaResult findPromotionName(Integer id, OperaResult result){
        result.getData().put("result", service.findPromotionName(id));
        return result;
    }
}
