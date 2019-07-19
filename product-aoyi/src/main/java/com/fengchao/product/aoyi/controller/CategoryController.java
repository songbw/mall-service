package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryX;
import com.fengchao.product.aoyi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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
    private OperaResult findOneLevelList(OperaResult result) {
        List<AoyiBaseCategoryX> categoryList = service.findOneLevelList();
        result.getData().put("list", categoryList) ;
        return result ;
    }

    @GetMapping("/subs")
    private OperaResult findSubList(Integer id, OperaResult result) {
        List<AoyiBaseCategoryX> categoryList = service.findTwoLevelListByOneLevelId(id);
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
