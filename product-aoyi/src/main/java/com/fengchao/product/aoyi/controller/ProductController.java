package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/prod", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/all")
    private OperaResult findList(@RequestBody ProductQueryBean queryBean, OperaResult result) throws ProductException {
        result.getData().put("result", service.findList(queryBean)) ;
        return result;
    }

    @GetMapping
    private OperaResult find(String mpu, OperaResult result) throws ProductException {
        if (StringUtils.isEmpty(mpu)) {
            throw new ProductException(200005, "mpu信息为null");
        }
        result.getData().put("result", service.findAndPromotion(mpu)) ;
        return result;
    }

    @PostMapping("/price")
    private OperaResult price(@RequestBody PriceQueryBean queryBean, OperaResult result) throws ProductException {
        return service.findPrice(queryBean);
    }

    /**
     *  库存
     * @param queryBean
     * @param result
     * @return
     */
    @PostMapping("/inventory")
    private OperaResult inventory(@RequestBody InventoryQueryBean queryBean, OperaResult result) throws ProductException {
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
    private OperaResult shipCarriage(@RequestBody CarriageQueryBean queryBean, OperaResult result) throws ProductException {
        result.getData().put("result", service.findCarriage(queryBean)) ;
        return result;
    }

    @GetMapping("/es")
    private OperaResult findAll(OperaResult result) throws ProductException {
        result.getData().put("result", service.findAll()) ;
        return result;
    }

}
