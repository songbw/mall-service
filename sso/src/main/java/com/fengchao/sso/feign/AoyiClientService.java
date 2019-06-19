package com.fengchao.sso.feign;

import com.fengchao.sso.feign.hystric.AoyiClientServiceH;
import com.fengchao.sso.util.OperaResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "aoyi-client", fallback = AoyiClientServiceH.class)
public interface AoyiClientService {

    @RequestMapping(value = "/order/conform", method = RequestMethod.GET)
    OperaResult conform(@RequestParam("orderId") String orderId);
}
