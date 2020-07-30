package com.fengchao.order.controller;

import com.fengchao.order.bean.*;
import com.fengchao.order.model.Order;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.service.OrderService;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单列表
 */
@RestController
@RequestMapping(value = "/third/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class ThirdOrderController {

    @Autowired
    private OrderService service;


    @PostMapping("logistics")
    private OperaResponse updateStatus(@RequestBody List<Logisticsbean> bean) {
        return service.logistics(bean);
    }

    @GetMapping("all")
    private OperaResponse getAll() {
        return service.getAll();
    }


}
