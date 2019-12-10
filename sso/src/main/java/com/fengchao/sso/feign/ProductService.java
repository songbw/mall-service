package com.fengchao.sso.feign;

import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.bean.Platform;
import com.fengchao.sso.feign.hystric.ProductServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author songbw
 * @date 2019/11/26 17:14
 */
@FeignClient(value = "product-aoyi", fallback = ProductServiceH.class)
public interface ProductService {

    @RequestMapping(value = "/platform/sub", method = RequestMethod.GET)
    OperaResponse<Platform> findPlatformBySubAppId(@RequestParam("appId") String appId);

}
