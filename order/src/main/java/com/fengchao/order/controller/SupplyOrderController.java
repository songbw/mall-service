package com.fengchao.order.controller;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.supply.SupplyOrderBean;
import com.fengchao.order.service.SupplyOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songbw
 * @date 2021/1/18 16:03
 */
@RestController
@RequestMapping(value = "/supply/order")
@Slf4j
public class SupplyOrderController {

    private final SupplyOrderService service ;

    @Autowired
    public SupplyOrderController(SupplyOrderService service) {
        this.service = service;
    }

    @PostMapping
    private OperaResponse add(@RequestBody SupplyOrderBean bean) {
        return service.preOrder(bean);
    }
}
