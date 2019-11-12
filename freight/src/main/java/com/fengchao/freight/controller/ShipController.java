package com.fengchao.freight.controller;

import com.fengchao.freight.bean.OperaResult;
import com.fengchao.freight.bean.ShipMpuParam;
import com.fengchao.freight.service.ShippingService;
import com.fengchao.freight.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/ship", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class ShipController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping ()
    public OperaResult getMpuShipping(@RequestBody List<ShipMpuParam> beans, OperaResult result){
        log.info("导出商品 入参:{}", JSONUtil.toJsonString(beans));
        result.getData().put("result",shippingService.getMpuShipping(beans));
        return result;
    }

    @PostMapping("template")
    public OperaResult getMpuTemplate(@RequestBody ShipMpuParam bean, OperaResult result){
        result.getData().put("result",shippingService.getMpuTemplate(bean));
        return result;
    }

    /**
     * 包邮按照平台包邮计算运费
     */
    @PostMapping ("carriage")
    public OperaResult getMpuCarriage(@RequestBody ShipMpuParam beans, OperaResult result){
        log.info("导出商品 入参:{}", JSONUtil.toJsonString(beans));
        result.getData().put("result",shippingService.getMpuCarriage(beans));
        return result;
    }
}
