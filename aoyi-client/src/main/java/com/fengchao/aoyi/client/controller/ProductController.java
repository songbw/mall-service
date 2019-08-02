package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.OperaResult;
import com.fengchao.aoyi.client.bean.QueryCarriage;
import com.fengchao.aoyi.client.bean.QueryCityPrice;
import com.fengchao.aoyi.client.bean.QueryInventory;
import com.fengchao.aoyi.client.exception.AoyiClientException;
import com.fengchao.aoyi.client.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController {

    @Autowired
    private ProductService service;


    @PostMapping("/price")
    private OperaResult price(@RequestBody QueryCityPrice queryBean, OperaResult result) throws AoyiClientException {
        return service.findPrice(queryBean);
    }

    /**
     *  库存
     * @param queryBean
     * @param result
     * @return
     */
    @PostMapping("/inventory")
    private OperaResult inventory(@RequestBody QueryInventory queryBean, OperaResult result) throws AoyiClientException {
        return service.findInventory(queryBean);
    }

    /**
     *  运费
     * @param queryBean
     * @param result
     * @return
     */
    @PostMapping("/carriage")
    private OperaResult shipCarriage(@RequestBody QueryCarriage queryBean, OperaResult result) throws AoyiClientException {
        return service.findCarriage(queryBean);
    }

    @GetMapping("/category")
    private OperaResult category(OperaResult result) throws AoyiClientException {
        return service.category();
    }

    @GetMapping("/skus")
    private OperaResult prodSkus(Integer categoryId, OperaResult result) throws AoyiClientException {
        return service.getProdSkuPool(categoryId);
    }

    @GetMapping("/image")
    private OperaResult image(String skuId, OperaResult result) throws AoyiClientException {
        return service.getProdImage(skuId);
    }

    @GetMapping("/detail")
    private OperaResult detail(String skuId, OperaResult result) throws AoyiClientException {
        return service.getProdDetail(skuId);
    }

    @GetMapping("/status")
    private OperaResult status(String skuId, OperaResult result) throws AoyiClientException {
        return service.getSaleStatus(skuId);
    }

}
