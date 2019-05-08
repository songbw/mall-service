package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/prod", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/all")
    private OperaResult findList(@RequestBody ProductQueryBean queryBean, OperaResult result) {
        result.getData().put("result", service.findList(queryBean)) ;
        return result;
    }

    @GetMapping
    private OperaResult find(String id, OperaResult result) {
        result.getData().put("result", service.findAndPromotion(id)) ;
        return result;
    }

    @PostMapping("/price")
    private OperaResult price(@RequestBody PriceQueryBean queryBean, OperaResult result) {
        return service.findPrice(queryBean);
    }

    /**
     *  库存
     * @param queryBean
     * @param result
     * @return
     */
    @PostMapping("/inventory")
    private OperaResult inventory(@RequestBody InventoryQueryBean queryBean, OperaResult result) {
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
    private OperaResult shipCarriage(@RequestBody CarriageQueryBean queryBean, OperaResult result) {
        result.getData().put("result", service.findCarriage(queryBean)) ;
        return result;
    }

}
