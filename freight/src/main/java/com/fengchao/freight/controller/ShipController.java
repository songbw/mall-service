package com.fengchao.freight.controller;

import com.fengchao.freight.bean.OperaResult;
import com.fengchao.freight.bean.ShipMpuParam;
import com.fengchao.freight.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/ship", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ShipController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping ()
    public OperaResult getMpuShipping(@RequestBody ShipMpuParam bean, OperaResult result){
        result.getData().put("shipPrice",shippingService.getMpuShipping(bean));
        return result;
    }

}
