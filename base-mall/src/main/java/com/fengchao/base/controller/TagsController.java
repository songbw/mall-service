package com.fengchao.base.controller;

import com.fengchao.base.bean.OperaResult;
import com.fengchao.base.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TagsController {

    @Autowired
    private TagsService service;

    @GetMapping
    private OperaResult find(OperaResult result) {
        result.getData().put("cdnUrl", service.findById(1)) ;
        return result;
    }

}
