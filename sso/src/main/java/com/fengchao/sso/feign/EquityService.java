package com.fengchao.sso.feign;

import com.fengchao.sso.bean.VirtualTicketsBean;
import com.fengchao.sso.feign.hystric.EquityServiceH;
import com.fengchao.sso.util.OperaResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "equity", fallback = EquityServiceH.class)
public interface EquityService {

    @RequestMapping(value = "/couponUseInfo/collectGiftCoupon", method = RequestMethod.GET)
    OperaResult findCollectGiftCouponByOpenId(@RequestParam("openId") String openId);

    /**
     * 生成用户虚拟商品
     * @param bean
     * @return
     */
    @RequestMapping(value = "/virtual/create", method = RequestMethod.POST)
    OperaResult createVirtual(@RequestBody VirtualTicketsBean bean);
}
