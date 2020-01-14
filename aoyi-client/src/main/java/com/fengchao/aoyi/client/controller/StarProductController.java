package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.QueryBean;
import com.fengchao.aoyi.client.exception.AoyiClientException;
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
    private OperaResponse getSpuIdList(@RequestBody QueryBean queryBean) throws AoyiClientException {
        return service.getSpuIdList(queryBean);
    }

    @GetMapping("/spu/detail")
    private OperaResponse getSpuDetail(String spuIds) throws AoyiClientException {
        return service.getSpuDetail(spuIds);
    }

    @GetMapping("/sku/detail")
    private OperaResponse getSkuDetail(String skuId) throws AoyiClientException {
        return service.getSkuListDetailBySpuId(skuId);
    }

    @PostMapping("/brand")
    private OperaResponse findBrandList(@RequestBody QueryBean queryBean) throws AoyiClientException {
        return service.findBrandList(queryBean);
    }

    @GetMapping("/category")
    private OperaResponse findProdCategory(String categoryId) throws AoyiClientException {
        return service.findProdCategory(categoryId);
    }

    @PostMapping("/inventory")
    private OperaResponse findSkuInventory(@RequestBody InventoryQueryBean queryBean) throws AoyiClientException {
        return service.findSkuInventory(queryBean);
    }

    @GetMapping("/price")
    private OperaResponse findSkuSalePrice(String codes) throws AoyiClientException {
        return service.findSkuSalePrice(codes);
    }

    @PostMapping("/address")
    private OperaResponse getAddressInfo(@RequestBody AddressInfoQueryBean bean) throws AoyiClientException {
        return service.getAddressInfo(bean);
    }

    @PostMapping("/inventory/hold")
    private OperaResponse preHoldSkuInventory(@RequestBody HoldSkuInventoryQueryBean bean) throws AoyiClientException {
        return service.preHoldSkuInventory(bean);
    }

    @PostMapping("/inventory/release")
    private OperaResponse releaseSkuInventory(@RequestBody ReleaseSkuInventoryQueryBean bean) throws AoyiClientException {
        return service.releaseSkuInventory(bean);
    }
}
