package com.fengchao.sso.feign;

import com.fengchao.sso.feign.hystric.EquityServiceH;
import com.fengchao.sso.util.OperaResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "equity", fallback = EquityServiceH.class)
public interface EquityService {

    @RequestMapping(value = "/couponUseInfo/collectGiftCoupon", method = RequestMethod.GET)
    OperaResult findCollectGiftCouponByOpenId(@RequestParam("openId") String openId);
}
