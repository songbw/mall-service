package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.bean.supply.SupplyBean;
import com.fengchao.product.aoyi.bean.supply.SupplyInventoryBean;
import com.fengchao.product.aoyi.dao.EnterpriseSyncBrandDao;
import com.fengchao.product.aoyi.service.*;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    private final EnterpriseSyncBrandService enterpriseSyncBrandService ;
    private final EnterpriseSyncCategoryService enterpriseSyncCategoryService ;
    private final EnterpriseSyncProductService enterpriseSyncProductService ;
    private final EnterpriseSyncSkuService enterpriseSyncSkuService ;

    @Autowired
    public SupplyProdController(CategoryService categoryService, BrandService brandService, SupplyProdService supplyProdService, EnterpriseSyncBrandDao enterpriseSyncBrandDao, EnterpriseSyncBrandService enterpriseSyncBrandService, EnterpriseSyncCategoryService enterpriseSyncCategoryService, EnterpriseSyncProductService enterpriseSyncProductService, EnterpriseSyncSkuService enterpriseSyncSkuService) {
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.supplyProdService = supplyProdService;
        this.enterpriseSyncBrandService = enterpriseSyncBrandService;
        this.enterpriseSyncCategoryService = enterpriseSyncCategoryService;
        this.enterpriseSyncProductService = enterpriseSyncProductService;
        this.enterpriseSyncSkuService = enterpriseSyncSkuService;
    }

    @PostMapping("brand")
    private OperaResponse findBrand(@RequestBody QueryBean queryBean) {
        log.debug("find brand 入参：{}", JSONUtil.toJsonString(queryBean));
        return brandService.selectBrandPageable(queryBean);
    }

    @PostMapping("notify/brand")
    private OperaResponse notifyBrand(@RequestBody EnterpriseSyncBrandNotifyBean notifyBean) {
        log.debug("notify brand 入参：{}", JSONUtil.toJsonString(notifyBean));
        return enterpriseSyncBrandService.notifyEnterpriseBrand(notifyBean);
    }

    @PostMapping("category")
    private OperaResponse findCategory(@RequestBody QueryBean queryBean) {
        log.debug("find category 入参：{}", JSONUtil.toJsonString(queryBean));
        return categoryService.queryCategoryPageable(queryBean);
    }

    @PostMapping("notify/category")
    private OperaResponse notifyCategory(@RequestBody EnterpriseSyncCategoryNotifyBean notifyBean) {
        log.debug("notify category 入参：{}", JSONUtil.toJsonString(notifyBean));
        return enterpriseSyncCategoryService.notifyEnterpriseCategory(notifyBean);
    }

    @PostMapping
    private OperaResponse findProduct(@RequestBody ProductQueryBean queryBean) {
        log.debug("find product 入参：{}", JSONUtil.toJsonString(queryBean));
        return supplyProdService.findProductPageable(queryBean);
    }

    @PostMapping("notify")
    private OperaResponse notifyProduct(@RequestBody EnterpriseSyncProductNotifyBean notifyBean) {
        log.debug("notify 入参：{}", JSONUtil.toJsonString(notifyBean));
        return enterpriseSyncProductService.notifyEnterpriseSpu(notifyBean);
    }

    @PostMapping("sku")
    private OperaResponse findSku(@RequestBody ProductQueryBean queryBean) {
        log.debug("find product 入参：{}", JSONUtil.toJsonString(queryBean));
        return supplyProdService.batchFindSkuBySpu(queryBean);
    }

    @PostMapping("notify/sku")
    private OperaResponse notifySku(@RequestBody EnterpriseSyncSkuNotifyBean notifyBean) {
        log.debug("notify sku 入参：{}", JSONUtil.toJsonString(notifyBean));
        return enterpriseSyncSkuService.notifyEnterpriseSku(notifyBean);
    }

    @PostMapping("price")
    private OperaResponse findSkuPrice(@RequestBody List<SupplyBean> supplyBeans) {
        log.debug("find product price 入参：{}", JSONUtil.toJsonString(supplyBeans));
        return supplyProdService.batchFindSkuPrice(supplyBeans);
    }

    @PostMapping("inventory")
    private OperaResponse findSkuInventory(@RequestBody SupplyInventoryBean inventoryBean) {
        log.debug("find product inventory 入参：{}", JSONUtil.toJsonString(inventoryBean));
        return supplyProdService.batchFindSkuInventory(inventoryBean);
    }
}
