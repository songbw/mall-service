package com.fengchao.order.feign;

import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.hystric.ProductServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "product-aoyi", fallback = ProductServiceH.class)
public interface ProductService {

    @RequestMapping(value = "/prod", method = RequestMethod.GET)
    OperaResult find(@RequestParam("id") String id);

}
