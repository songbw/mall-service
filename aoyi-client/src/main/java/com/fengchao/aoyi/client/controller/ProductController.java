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
    private OperaResult inventory(@RequestBody QueryInventory queryBean, OperaResult result) throws AoyiClientException {
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
    private OperaResult shipCarriage(@RequestBody QueryCarriage queryBean, OperaResult result) throws AoyiClientException {
        result.getData().put("result", service.findCarriage(queryBean)) ;
        return result;
    }

    @GetMapping("/category")
    private OperaResult category(OperaResult result) throws AoyiClientException {
        result.getData().put("result", service.category()) ;
        return result;
    }

    @GetMapping("/skus")
    private OperaResult prodSkus(Integer categoryId, OperaResult result) throws AoyiClientException {
        result.getData().put("result", service.getProdSkuPool(categoryId)) ;
        return result;
    }

    @GetMapping("/image")
    private OperaResult image(String skuId, OperaResult result) throws AoyiClientException {
        result.getData().put("result", service.getProdImage(skuId)) ;
        return result;
    }

    @GetMapping("/detail")
    private OperaResult detail(String skuId, OperaResult result) throws AoyiClientException {
        result.getData().put("result", service.getProdDetail(skuId)) ;
        return result;
    }

    @GetMapping("/status")
    private OperaResult status(String skuId, OperaResult result) throws AoyiClientException {
        result.getData().put("result", service.getSaleStatus(skuId)) ;
        return result;
    }

}
