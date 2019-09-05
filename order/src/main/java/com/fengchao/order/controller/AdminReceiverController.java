package com.fengchao.order.controller;

import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.service.AdminReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/admin/receiver", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminReceiverController {

    @Autowired
    private AdminReceiverService service;

    @GetMapping("user")
    private OperaResult findByOpenId(String openId, OperaResult result) {
        result.getData().put("result", service.findByOpenId(openId)) ;
        return result;
    }

    @GetMapping
    private OperaResult find(Integer id, OperaResult result) {
        result.getData().put("result", service.find(id)) ;
        return result;
    }

}
