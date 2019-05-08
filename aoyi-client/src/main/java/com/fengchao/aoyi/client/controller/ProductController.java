package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.*;
import com.fengchao.aoyi.client.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController {

    @Autowired
    private ProductService service;


    @PostMapping("/price")
    private OperaResult price(@RequestBody QueryCityPrice queryBean, OperaResult result) {
        result.getData().put("result", service.findPrice(queryBean)) ;
        return result;
    }

    /**
     *  库存
     * @param queryBean
     * @param result
     * @return
     */
    @PostMapping("/inventory")
    private OperaResult inventory(@RequestBody QueryInventory queryBean, OperaResult result) {
        result.getData().put("result", service.findInventory(queryBean)) ;
        return result;
    }

    /**
     *  运费
     * @param queryBean
     * @param result
     * @return
     */
    @PostMapping("/carriage")
    private OperaResult shipCarriage(@RequestBody QueryCarriage queryBean, OperaResult result) {
        result.getData().put("result", service.findCarriage(queryBean)) ;
        return result;
    }

}
