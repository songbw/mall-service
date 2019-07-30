package com.fengchao.equity.controller;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.ThirdOffShelfResult;
import com.fengchao.equity.bean.ThirdResult;
import com.fengchao.equity.bean.ToushiResult;
import com.fengchao.equity.exception.EquityException;
import com.fengchao.equity.service.CouponThirdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/coupon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class CouponThirdController {

    @Autowired
    private CouponThirdService service;

    @PostMapping("consumed")//头食对接接口
    public OperaResult consumed(@RequestBody ToushiResult bean) throws EquityException {
//        result.getData().put("result",num);
        return service.consumedToushi(bean);
    }

    @PostMapping("obtain")//头食对接接口
    public OperaResult obtainCoupon(@RequestBody ToushiResult bean) throws EquityException{
        return service.obtainCoupon(bean);
    }

    @PostMapping("user_verified")//头食对接接口
    public OperaResult userVerified(@RequestBody ToushiResult bean) throws EquityException{
        return service.userVerified(bean);
    }

    @PostMapping("equityCreate")//头食对接接口
    public OperaResult equityCreate(@RequestBody ThirdResult bean) throws EquityException{
        return service.equityCreate(bean);
    }

    @PostMapping("offShelf")//头食对接接口
    public OperaResult equityOffShelf(@RequestBody ThirdOffShelfResult bean) throws EquityException{
        return service.equityOffShelf(bean);
    }
}
