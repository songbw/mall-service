package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.SkuCode;
import com.fengchao.product.aoyi.service.BrandService;
import com.fengchao.product.aoyi.service.SkuCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/merchantCode", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SkuCodeController {

    @Autowired
    private SkuCodeService service;

    @GetMapping
    private OperaResult find(Integer id, OperaResult result) {
        SkuCode skuCode = service.find(id);
        result.getData().put("result", skuCode) ;
        return result;
    }

    @GetMapping("all")
    private OperaResult findAll(OperaResult result) {
        result.getData().put("result", service.findAll()) ;
        return result;
    }

    @GetMapping("merchant")
    private OperaResult findByMerchant(Integer merchantId, OperaResult result) {
        SkuCode skuCode = service.findByMerchantId(merchantId);
        result.getData().put("result", skuCode) ;
        return result;
    }

    @PostMapping
    private OperaResult create(@RequestBody SkuCode bean, OperaResult result) throws ProductException {
        int id = service.add(bean) ;
        result.getData().put("result", id) ;
        return result;
    }

    @PutMapping
    private OperaResult update(@RequestBody SkuCode bean, OperaResult result) throws ProductException {
        int id = service.update(bean) ;
        result.getData().put("result", id) ;
        return result;
    }

}
