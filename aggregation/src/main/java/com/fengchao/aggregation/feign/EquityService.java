package com.fengchao.aggregation.feign;

import com.fengchao.aggregation.bean.OperaResult;
import com.fengchao.aggregation.feign.hystric.EquityServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "equity", fallback = EquityServiceH.class)
public interface EquityService {

    @RequestMapping(value = "/promotion/findOnline", method = RequestMethod.GET)
    OperaResult findOnlinePromotion(@RequestHeader("appId") String appId);
}

