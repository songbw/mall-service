package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.starBean.*;
import com.fengchao.aoyi.client.startService.OrderStarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author songbw
 * @date 2020/1/13 18:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/star/orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StarOrderController {

    @Autowired
    private OrderStarService service ;

    @PostMapping
    private OperaResponse addOrder(@RequestBody StarOrderBean bean) {
        return service.addOrder(bean);
    }

    @GetMapping("receive")
    private OperaResponse confirmOrderReceive(String orderSn) {
        return service.confirmOrder(orderSn);
    }

    @PostMapping("/apply/refund/goods")
    private OperaResponse applyRefundAndGoods(@RequestBody ApplyRefundAndGoodsBean bean) {
        return service.applyRefundAndGoods(bean);
    }

    @PostMapping("/apply/refund")
    private OperaResponse applyRefund(@RequestBody ApplyRefundAndGoodsBean bean) {
        return service.applyRefund(bean);
    }

    @PostMapping("/freight")
    private OperaResponse getOrderFreight(@RequestBody FreightBean bean) {
        return service.getOrderFreight(bean);
    }

    @PostMapping("/cancel/refund")
    private OperaResponse cancelApplyRefund(@RequestBody  CancelApplyRefundBean bean) {
        return service.cancelApplyRefund(bean);
    }

    @GetMapping("express")
    private OperaResponse findExpressInfoByOrderSn(String orderSn) {
        return service.findExpressInfoByOrderSn(orderSn);
    }

    @GetMapping
    private OperaResponse findOrderByOrderSn(String orderSn) {
        return service.findExpressInfoByOrderSn(orderSn);
    }

    @GetMapping("logistics/company")
    private OperaResponse getLogisticsCompanyList() {
        return service.getLogisticsCompanyList();
    }

    @GetMapping("return/status")
    private OperaResponse getReturnOrderStatuts(String serviceSn) {
        return service.getReturnOrderStatuts(serviceSn);
    }

    @PostMapping("/return/status")
    private OperaResponse getReturnOrderStatutsNotify(@RequestBody ReturnOrderGoodsBean bean) {
        return service.getReturnOrderStatutsNotify(bean);
    }
}
