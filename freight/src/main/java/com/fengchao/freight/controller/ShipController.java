package com.fengchao.freight.controller;

import com.fengchao.freight.bean.OperaResult;
import com.fengchao.freight.bean.ShipMpuParam;
import com.fengchao.freight.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/ship", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ShipController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping ()
    public OperaResult getMpuShipping(@RequestBody List<ShipMpuParam> beans, OperaResult result){
        result.getData().put("result",shippingService.getMpuShipping(beans));
        return result;
    }

}
