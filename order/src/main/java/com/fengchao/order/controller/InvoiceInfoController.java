package com.fengchao.order.controller;

import com.fengchao.order.bean.InvoiceInfoQueryBean;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.model.InvoiceInfo;
import com.fengchao.order.service.InvoiceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 发票信息接口
 */
@RestController
@RequestMapping(value = "/invoice", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class InvoiceInfoController {

    @Autowired
    private InvoiceInfoService service;

    @PostMapping("/all")
    private OperaResult find(@RequestBody InvoiceInfoQueryBean queryBean, OperaResult result) {
        result.getData().put("result", service.findList(queryBean)) ;
        return result;
    }

    @PostMapping
    private OperaResult add(@RequestBody InvoiceInfo bean, OperaResult result) {
        result.getData().put("result", service.add(bean)) ;
        return result;
    }

    @DeleteMapping
    private OperaResult delete(Integer id, OperaResult result) {
        result.getData().put("result", service.delete(id)) ;
        return result;
    }

    @PutMapping
    private OperaResult modify(@RequestBody InvoiceInfo bean, OperaResult result) {
        result.getData().put("result", service.modify(bean)) ;
        return result;
    }

    @GetMapping
    private OperaResult find(Integer id, OperaResult result) {
        result.getData().put("result", service.find(id)) ;
        return result;
    }

}
