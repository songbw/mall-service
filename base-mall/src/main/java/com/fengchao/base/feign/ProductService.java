package com.fengchao.base.feign;

import com.fengchao.base.bean.OperaResponse;
import com.fengchao.base.feign.hystric.ProductServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "product-aoyi", fallback = ProductServiceH.class)
public interface ProductService {

    @RequestMapping(value = "/prod", method = RequestMethod.GET)
    OperaResponse imageBack(@RequestParam("id") Long id, @RequestParam("status") Integer status);


}
