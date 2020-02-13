package com.fengchao.equity.controller;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.model.CardAndCoupon;
import com.fengchao.equity.service.CardAndCouponService;
import com.fengchao.equity.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/adminCardAndCoupon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminCardAndCouponController {

    @Autowired
    private CardAndCouponService service;

    @PostMapping("create")
    public OperaResult createCardAndCoupon(@RequestBody CardAndCoupon bean, OperaResult result){
        log.info("创建CardAndCoupon参数 入参:{}", JSONUtil.toJsonString(bean));
        result.getData().put("result",service.createCardAndCoupon(bean));
        return result;
    }

    @PutMapping("update")
    public OperaResult updateCardAndCoupon(@RequestBody CardAndCoupon bean, OperaResult result){
        result.getData().put("result",service.updateCardAndCoupon(bean));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteCardAndCoupon(Integer id, OperaResult result){
        result.getData().put("result",service.deleteCardAndCoupon(id));
        return result;
    }
}
