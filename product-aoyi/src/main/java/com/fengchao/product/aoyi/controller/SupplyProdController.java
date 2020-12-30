package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.bean.QueryBean;
import com.fengchao.product.aoyi.service.*;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 产品供应
 * @author songbw
 * @date 2020/12/28 14:36
 */
@RestController
@RequestMapping(value = "/supply/prod", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class SupplyProdController {

    private final CategoryService categoryService;
    private final BrandService brandService ;
    private final SupplyProdService supplyProdService ;

    @Autowired
    public SupplyProdController(CategoryService categoryService, BrandService brandService, SupplyProdService supplyProdService) {
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.supplyProdService = supplyProdService;
    }

    @GetMapping("brand")
    private OperaResponse findBrand(QueryBean queryBean) {
        log.debug("find brand 入参：{}", JSONUtil.toJsonString(queryBean));
        return brandService.selectBrandPageable(queryBean);
    }

    @PostMapping("notify/brand")
    private OperaResponse notifyBrand(List<Integer> brandIds) {
        log.debug("notify brand 入参：{}", JSONUtil.toJsonString(brandIds));
        return brandService.selectBrandPageable( new QueryBean());
    }

    @GetMapping("category")
    private OperaResponse findCategory(QueryBean queryBean) {
        log.debug("find category 入参：{}", JSONUtil.toJsonString(queryBean));
        return categoryService.queryCategoryPageable(queryBean);
    }

    @GetMapping
    private OperaResponse findProduct(ProductQueryBean queryBean) {
        log.debug("find product 入参：{}", JSONUtil.toJsonString(queryBean));
        return supplyProdService.findProductPageable(queryBean);
    }
}
