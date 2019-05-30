package com.fengchao.elasticsearch.feign;

import com.fengchao.elasticsearch.domain.OperaResult;
import com.fengchao.elasticsearch.domain.ProductQueryBean;
import com.fengchao.elasticsearch.feign.hystric.ProductFeignH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "product-aoyi", fallback = ProductFeignH.class)
public interface ProductFeign {

    @RequestMapping(value = "/prod/es", method = RequestMethod.GET)
    OperaResult getAllEsProductList();

}
