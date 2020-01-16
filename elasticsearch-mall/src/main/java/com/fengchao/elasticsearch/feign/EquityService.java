package com.fengchao.elasticsearch.feign;

import com.fengchao.elasticsearch.domain.AoyiProdIndex;
import com.fengchao.elasticsearch.domain.OperaResult;
import com.fengchao.elasticsearch.feign.hystric.EquityServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "equity", fallback = EquityServiceH.class)
public interface EquityService {

    @RequestMapping(value = "/promotion/mpu", method = RequestMethod.GET)
    OperaResult findPromotionBySkuId(@RequestParam("skuId") String skuId, @RequestHeader("appId") String appId);

    @RequestMapping(value = "/adminCoupon/findByMpu", method = RequestMethod.POST)
    OperaResult selectCouponBySku(@RequestBody AoyiProdIndex bean, @RequestHeader("appId") String appId);
}
