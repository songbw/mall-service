package com.fengchao.product.aoyi.feign;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.feign.hystric.AoyiClientServiceClientFallbackFactory;
import com.fengchao.product.aoyi.feign.hystric.AoyiClientServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "aoyi-client", url = "${rpc.feign.client.aoyiclient.url:}", fallbackFactory = AoyiClientServiceClientFallbackFactory.class)
public interface AoyiClientService {

    @RequestMapping(value = "/product/price", method = RequestMethod.POST)
    OperaResponse price(@RequestBody QueryCityPrice queryBean);

    @RequestMapping(value = "/product/inventory", method = RequestMethod.POST)
    OperaResponse<InventoryBean> inventory(@RequestBody QueryInventory queryBean);

    @RequestMapping(value = "/product/carriage", method = RequestMethod.POST)
    OperaResponse<FreightFareBean> shipCarriage(@RequestBody QueryCarriage queryBean);

    @RequestMapping(value = "/product/priceGAT", method = RequestMethod.POST)
    OperaResponse priceGAT(@RequestBody QueryCityPrice queryBean);

    @RequestMapping(value = "/star/product/spus", method = RequestMethod.POST)
    OperaResponse getSpuIdList(@RequestBody QueryBean queryBean);

    @RequestMapping(value = "/star/product/spu/detail", method = RequestMethod.GET)
    OperaResponse getSpuDetail(@RequestParam("spuIds") String spuIds);

    @RequestMapping(value = "/star/product/sku/detail", method = RequestMethod.GET)
    OperaResponse getSkuDetail(@RequestParam("skuId") String skuId);

    @RequestMapping(value = "/star/product/brand", method = RequestMethod.POST)
    OperaResponse findBrandList(@RequestBody QueryBean queryBean);

    @RequestMapping(value = "/star/product/category", method = RequestMethod.GET)
    OperaResponse findProdCategory(@RequestParam("categoryId") String categoryId);

    // 唯品会 begin
    @RequestMapping(value = "/weipinhui/getBrand", method = RequestMethod.GET)
    OperaResponse weipinhuiGetBrand(@RequestParam("pageNumber") Integer pageNumber,
                                    @RequestParam("pageSize") Integer pageSize);

    // 唯品会 end
}
