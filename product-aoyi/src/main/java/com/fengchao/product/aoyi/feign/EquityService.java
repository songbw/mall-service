package com.fengchao.product.aoyi.feign;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.feign.hystric.EquityServiceH;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "equity", fallback = EquityServiceH.class)
public interface EquityService {

    @RequestMapping(value = "/promotion/mpu", method = RequestMethod.GET)
    OperaResult findPromotionBySkuId(@RequestParam("skuId") String skuId, @RequestHeader("appId") String appId);

    @RequestMapping(value = "/adminCoupon/findByMpu", method = RequestMethod.POST)
    OperaResult selectCouponBySku(@RequestBody AoyiProdIndexX bean, @RequestHeader("appId") String appId);
}
