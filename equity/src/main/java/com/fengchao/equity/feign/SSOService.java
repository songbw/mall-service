package com.fengchao.equity.feign;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.feign.hystric.SSOServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "sso", fallback = SSOServiceH.class)
public interface SSOService {

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    OperaResult findUser(@RequestParam("openId") String openId, @RequestParam("iAppId")String iAppId);
}
