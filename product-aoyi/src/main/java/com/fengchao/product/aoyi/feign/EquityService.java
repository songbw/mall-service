package com.fengchao.product.aoyi.feign;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.feign.hystric.EquityServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "equity", fallback = EquityServiceH.class)
public interface EquityService {

    @RequestMapping(value = "/promotion/sku", method = RequestMethod.GET)
    OperaResult findPromotionBySkuId(@RequestParam("skuId") String skuId);
}
