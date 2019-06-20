package com.fengchao.order.controller;

import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.bean.RefundOrderQueryBean;
import com.fengchao.order.model.RefundOrder;
import com.fengchao.order.service.RefundOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/refund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RefundOrderController {

    @Autowired
    private RefundOrderService service;

    @PostMapping("/all")
    private OperaResult find(@RequestBody RefundOrderQueryBean queryBean, OperaResult result) {
        result.getData().put("result", service.findList(queryBean)) ;
        return result;
    }

    @PostMapping
    private OperaResult add(@RequestBody RefundOrder bean, OperaResult result) {
        int id = service.add(bean);
        if (id == 0) {
            result.setMsg("订单审核中，请不要重复提交！");
            result.setCode(id);
        } else {
            result.getData().put("result", id) ;
        }
        return result;
    }

    @DeleteMapping
    private OperaResult delete(Integer id, OperaResult result) {
        result.getData().put("result", service.delete(id)) ;
        return result;
    }

    @GetMapping("/subOrder")
    private OperaResult findBySubOrder(String subOrderId, OperaResult result) {
        result.getData().put("result", service.findBySubOrderId(subOrderId)) ;
        return result;
    }

    @PutMapping("/status")
    private OperaResult updateStatus(@RequestBody RefundOrder bean, OperaResult result) {
        result.getData().put("result", service.updateStatus(bean)) ;
        return result;
    }

    @PutMapping
    private OperaResult update(@RequestBody RefundOrder bean, OperaResult result) {
        result.getData().put("result", service.update(bean)) ;
        return result;
    }

}
