package com.fengchao.order.controller;

import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.bean.ShoppingCartQueryBean;
import com.fengchao.order.model.ShoppingCart;
import com.fengchao.order.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService service;

    @PostMapping("/all")
    private OperaResult find(@RequestBody ShoppingCartQueryBean queryBean, OperaResult result) {
        result.getData().put("result", service.findList(queryBean)) ;
        return result;
    }

    @PostMapping
    private OperaResult add(@RequestBody ShoppingCart bean) {
        return service.add(bean);
    }

    @DeleteMapping
    private OperaResult delete(Integer id, OperaResult result) {
        result.getData().put("result", service.delete(id)) ;
        return result;
    }

    @PutMapping("/num")
    private OperaResult modifyNum(@RequestBody ShoppingCart bean) {
        return service.modifyNum(bean);
    }

}
