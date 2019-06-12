package com.fengchao.order.controller;

import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.bean.ReceiverQueryBean;
import com.fengchao.order.model.Receiver;
import com.fengchao.order.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 收货地址接口
 */
@RestController
@RequestMapping(value = "/receiver", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ReceiverController {

    @Autowired
    private ReceiverService service;

    @PostMapping("/all")
    private OperaResult findList(@RequestBody ReceiverQueryBean queryBean, OperaResult result) {
        result.getData().put("result", service.findList(queryBean)) ;
        return result;
    }

    @GetMapping
    private OperaResult find(Integer id, OperaResult result) {
        result.getData().put("result", service.find(id)) ;
        return result;
    }

    @PostMapping
    private OperaResult add(@RequestBody Receiver bean, OperaResult result) {
        result.getData().put("result", service.add(bean)) ;
        return result;
    }

    @DeleteMapping
    private OperaResult delete(Integer id, OperaResult result) {
        result.getData().put("result", service.delete(id)) ;
        return result;
    }

    @PutMapping
    private OperaResult modify(@RequestBody Receiver bean, OperaResult result) {
        result.getData().put("result", service.modify(bean)) ;
        return result;
    }

    @PutMapping("/default")
    private OperaResult setDefault(@RequestBody Receiver bean, OperaResult result) {
        result.getData().put("result", service.setDefault(bean)) ;
        return result;
    }

}
