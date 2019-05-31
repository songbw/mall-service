package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/brand", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BrandController {

    @Autowired
    private BrandService service;

    @GetMapping
    private OperaResult find(OperaResult result) {
        return result;
    }
}
