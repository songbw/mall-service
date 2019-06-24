package com.fengchao.order.feign;

import com.fengchao.order.bean.CouponUseInfoBean;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.hystric.EquityServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "equity", fallback = EquityServiceH.class)
public interface EquityService {

    @RequestMapping(value = "/coupon/consume", method = RequestMethod.POST)
    OperaResult consume(@RequestBody CouponUseInfoBean bean);

}
