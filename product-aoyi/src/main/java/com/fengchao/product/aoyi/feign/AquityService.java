package com.fengchao.product.aoyi.feign;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.feign.hystric.AquityServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "equity", fallback = AquityServiceH.class)
public interface AquityService {

    @RequestMapping(value = "/promotion/sku", method = RequestMethod.GET)
    OperaResult find(@RequestParam("skuId") String skuId);
}
