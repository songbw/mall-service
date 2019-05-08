package com.fengchao.product.aoyi.feign;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.QueryCarriage;
import com.fengchao.product.aoyi.bean.QueryCityPrice;
import com.fengchao.product.aoyi.bean.QueryInventory;
import com.fengchao.product.aoyi.feign.hystric.AoyiClientServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "aoyi-client", fallback = AoyiClientServiceH.class)
public interface AoyiClientService {

    @RequestMapping(value = "/product/price", method = RequestMethod.POST)
    OperaResult price(@RequestBody QueryCityPrice queryBean);

    @RequestMapping(value = "/product/inventory", method = RequestMethod.POST)
    OperaResult inventory(@RequestBody QueryInventory queryBean);

    @RequestMapping(value = "/product/carriage", method = RequestMethod.POST)
    OperaResult shipCarriage(@RequestBody QueryCarriage queryBean);
}
