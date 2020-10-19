package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryX;
import com.fengchao.product.aoyi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    private OperaResult find(OperaResult result) {
        return result;
    }

    @GetMapping("/ones")
    private OperaResult findOneLevelList(@RequestHeader("appId") String appId) {
        OperaResult result = new OperaResult();
        List<AoyiBaseCategoryX> categoryList = service.findOneLevelByAppId(appId);
        result.getData().put("list", categoryList) ;
        return result ;
    }

    @GetMapping("/subs")
    private OperaResult findSubList(@RequestHeader("appId") String appId, Integer id) {
        OperaResult result = new OperaResult();
        List<AoyiBaseCategoryX> categoryList = service.findTwoLevelByAppId(appId, id);
        result.getData().put("list", categoryList) ;
        return result ;
    }

    @GetMapping("/allsub")
    private OperaResult findList(Integer id, OperaResult result) {
        List<AoyiBaseCategoryX> categoryList = service.findListById(id);
        result.getData().put("list", categoryList) ;
        return result ;
    }

}
