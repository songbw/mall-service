package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.OperaResult;
import com.fengchao.aoyi.client.bean.OrderParamBean;
import com.fengchao.aoyi.client.bean.QueryLogist;
import com.fengchao.aoyi.client.exception.AoyiClientException;
import com.fengchao.aoyi.client.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {

    @Autowired
    private OrderService service;

    @GetMapping("/conform")
    private OperaResult conform(String orderId, OperaResult result) throws AoyiClientException {
        result.setData(service.confirmOrder(orderId)) ;
        return result;
    }

    @PostMapping("/logist")
    private OperaResult logist(@RequestBody QueryLogist queryLogist, OperaResult result) throws AoyiClientException {
        result.setData(service.getOrderLogist(queryLogist)) ;
        return result;
    }

    @PostMapping
    private OperaResult create(@RequestBody OrderParamBean orderParamBean, OperaResult result) throws AoyiClientException {
        return service.addOrder(orderParamBean);
    }
}
