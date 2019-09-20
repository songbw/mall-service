package com.fengchao.freight.controller;

import com.fengchao.freight.bean.FreeShipTemplateBean;
import com.fengchao.freight.bean.OperaResult;
import com.fengchao.freight.bean.ShipTemplateBean;
import com.fengchao.freight.service.FreeShippingService;
import com.fengchao.freight.service.ShippingService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminFreeShip", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminFreeShippingController {

    @Autowired
    private FreeShippingService service;

    @PostMapping("create")
    public OperaResult createFreeShipTemplate(@RequestBody FreeShipTemplateBean bean, OperaResult result){
        result.getData().put("templateId",service.createFreeShipTemplate(bean));
        return result;
    }

    @GetMapping("find")
    public OperaResult findFreeShipTemplate(@Param("pageNo") Integer pageNo, @Param("pageSize")Integer pageSize, OperaResult result){
        result.getData().put("result",service.findFreeShipTemplate(pageNo, pageSize));
        return result;
    }

    @GetMapping("findById")
    public OperaResult findFreeShipTemplateById(Integer id, OperaResult result){
        result.getData().put("result",service.findFreeShipTemplateById(id));
        return result;
    }

    @PutMapping("update")
    public OperaResult updateFreeShipTemplate(@RequestBody FreeShipTemplateBean bean, OperaResult result){
        result.getData().put("result",service.updateFreeShipTemplate(bean));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteFreeShipTemplate(Integer id, OperaResult result){
        result.getData().put("result",service.deleteFreeShipTemplate(id));
        return result;
    }

    @DeleteMapping("deleteRegions")
    public OperaResult deleteShipRegions(Integer id, OperaResult result){
        result.getData().put("result",service.deleteShipRegions(id));
        return result;
    }
}
