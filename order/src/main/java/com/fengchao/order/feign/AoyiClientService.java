package com.fengchao.order.feign;

import com.fengchao.order.bean.*;
import com.fengchao.order.feign.hystric.AoyiClientServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "aoyi-client", fallback = AoyiClientServiceH.class)
public interface AoyiClientService {

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    OperaResponse<List<SubOrderT>> order(@RequestBody OrderParamBean orderParamBean);

    @RequestMapping(value = "/product/price", method = RequestMethod.POST)
    OperaResponse<List<PriceBean>> price(@RequestBody QueryCityPrice queryBean);

    @RequestMapping(value = "/product/inventory", method = RequestMethod.POST)
    OperaResponse<InventoryBean> inventory(@RequestBody QueryInventory queryBean);

    @RequestMapping(value = "/product/carriage", method = RequestMethod.POST)
    OperaResponse<FreightFareBean> shipCarriage(@RequestBody QueryCarriage queryBean);

    @RequestMapping(value = "/order/gat", method = RequestMethod.POST)
    OperaResponse<List<SubOrderT>> orderGAT(@RequestBody OrderParamBean orderParamBean);

}
