package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.SkuCode;
import com.fengchao.product.aoyi.service.SkuCodeService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/renter/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class RenterCategoryController {

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
        log.info("获取所有商户信息 入参:无");
        List<SkuCode> skuCodeList = service.findAll();
        result.getData().put("result", skuCodeList);
        log.info("获取所有商户信息 返回:{}", JSONUtil.toJsonString(result));
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
