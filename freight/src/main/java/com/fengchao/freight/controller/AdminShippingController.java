package com.fengchao.freight.controller;

import com.fengchao.freight.bean.OperaResult;
import com.fengchao.freight.bean.ShipTemplateBean;
import com.fengchao.freight.service.ShippingService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminShip", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminShippingController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping("create")
    public OperaResult createShipTemplate(@RequestBody ShipTemplateBean bean, OperaResult result){
        result.getData().put("templateId",shippingService.createShipTemplate(bean));
        return result;
    }

    @GetMapping("find")
    public OperaResult findShipTemplate(@Param("pageNo") Integer pageNo, @Param("pageSize")Integer pageSize, OperaResult result){
        result.getData().put("result",shippingService.findShipTemplate(pageNo, pageSize));
        return result;
    }

    @GetMapping("findById")
    public OperaResult findShipTemplateById(Integer id, OperaResult result){
        result.getData().put("result",shippingService.findShipTemplateById(id));
        return result;
    }

    @PutMapping("update")
    public OperaResult updateShipTemplate(@RequestBody ShipTemplateBean bean, OperaResult result){
        result.getData().put("result",shippingService.updateShipTemplate(bean));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteShipTemplate(Integer id, OperaResult result){
        result.getData().put("result",shippingService.deleteShipTemplate(id));
        return result;
    }

    @PostMapping("createRegions")
    public OperaResult createShipRegions(@RequestBody ShipTemplateBean bean, OperaResult result){
        result.getData().put("result",shippingService.createShipRegions(bean));
        return result;
    }

    @DeleteMapping("deleteRegions")
    public OperaResult deleteShipRegions(Integer id, OperaResult result){
        result.getData().put("result",shippingService.deleteShipRegions(id));
        return result;
    }
}
