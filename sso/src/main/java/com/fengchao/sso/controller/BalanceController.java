package com.fengchao.sso.controller;

import com.fengchao.sso.bean.BalanceBean;
import com.fengchao.sso.bean.BalanceDetailQueryBean;
import com.fengchao.sso.bean.BalanceQueryBean;
import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.model.Balance;
import com.fengchao.sso.model.BalanceDetail;
import com.fengchao.sso.service.IBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/balance", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BalanceController {

    @Autowired
    private IBalanceService service;

    @PostMapping("/all")
    private OperaResponse find(@RequestBody BalanceQueryBean queryBean) {
        return service.findList(queryBean);
    }

    @PostMapping("/detail/all")
    private OperaResponse findDetailList(@RequestBody BalanceDetailQueryBean queryBean) {
        return service.findDetailList(queryBean);
    }

    @PostMapping
    private OperaResponse add(@RequestBody Balance bean) {
        return service.add(bean);
    }

    @PutMapping
    private OperaResponse update(@RequestBody BalanceBean bean) {
        return service.update(bean);
    }

    @PutMapping("consume")
    private OperaResponse consume(@RequestBody BalanceDetail bean) {
        return service.consume(bean);
    }

    @PutMapping("refund")
    private OperaResponse refund(@RequestBody BalanceDetail bean) {
        return service.refund(bean);
    }

    @GetMapping
    private OperaResponse findByOpenId(String openId) {
        return service.findByOpenId(openId);
    }

    @PutMapping("/detail/status")
    private OperaResponse updateDetailStatus(@RequestBody BalanceDetail bean) {
        return service.updateDetailStatus(bean);
    }

    @PostMapping("init")
    private OperaResponse init(@RequestBody BalanceBean bean, @RequestHeader("username") String username) {
        bean.setUsername(username);
        return service.init(bean);
    }

}
