package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.QueryBean;
import com.fengchao.aoyi.client.starBean.AddressInfoQueryBean;
import com.fengchao.aoyi.client.starBean.HoldSkuInventoryQueryBean;
import com.fengchao.aoyi.client.starBean.InventoryQueryBean;
import com.fengchao.aoyi.client.starBean.ReleaseSkuInventoryQueryBean;
import com.fengchao.aoyi.client.startService.ProductStarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author songbw
 * @date 2020/1/13 18:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/star/product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StarProductController {

    @Autowired
    private ProductStarService service ;

    @PostMapping("/spus")
    private OperaResponse getSpuIdList(@RequestBody QueryBean queryBean) {
        return service.getSpuIdList(queryBean);
    }

    @GetMapping("/spu/detail")
    private OperaResponse getSpuDetail(String spuIds) {
        return service.getSpuDetail(spuIds);
    }

    @GetMapping("/sku/detail")
    private OperaResponse getSkuDetail(String skuId) {
        return service.getSkuListDetailBySpuId(skuId);
    }

    @PostMapping("/brand")
    private OperaResponse findBrandList(@RequestBody QueryBean queryBean) {
        return service.findBrandList(queryBean);
    }

    @GetMapping("/category")
    private OperaResponse findProdCategory(String categoryId) {
        return service.findProdCategory(categoryId);
    }

    @PostMapping("/inventory")
    private OperaResponse findSkuInventory(@RequestBody InventoryQueryBean queryBean) {
        return service.findSkuInventory(queryBean);
    }

    @GetMapping("/price")
    private OperaResponse findSkuSalePrice(String codes) {
        return service.findSkuSalePrice(codes);
    }

    @PostMapping("/address")
    private OperaResponse getAddressInfo(@RequestBody AddressInfoQueryBean bean) {
        return service.getAddressInfo(bean);
    }

    @PostMapping("/inventory/hold")
    private OperaResponse preHoldSkuInventory(@RequestBody HoldSkuInventoryQueryBean bean) {
        return service.preHoldSkuInventory(bean);
    }

    @PostMapping("/inventory/release")
    private OperaResponse releaseSkuInventory(@RequestBody ReleaseSkuInventoryQueryBean bean) {
        return service.releaseSkuInventory(bean);
    }
}
