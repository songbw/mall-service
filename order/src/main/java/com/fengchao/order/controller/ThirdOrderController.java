package com.fengchao.order.controller;

import com.fengchao.order.bean.Logisticsbean;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.model.Order;
import com.fengchao.order.model.Orders;
import com.fengchao.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单列表
 */
@RestController
@RequestMapping(value = "/third/order")
@Slf4j
public class ThirdOrderController {

    @Autowired
    private OrderService service;


    @PostMapping(value = "logistics", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private OperaResponse updateStatus(@RequestBody List<Logisticsbean> bean) {
        return service.logistics(bean);
    }

    @PostMapping
    private OperaResponse add(@RequestBody Order bean) {
        return service.syncAdd(bean);
    }


}
