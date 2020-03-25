package com.fengchao.freight.controller;

import com.alibaba.fastjson.JSON;
import com.fengchao.freight.bean.OperaResult;
import com.fengchao.freight.bean.ShipTemplateBean;
import com.fengchao.freight.service.ShippingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
    public OperaResult findShipTemplate0(@Param("pageNo") Integer pageNo, @Param("pageSize")Integer pageSize, OperaResult result){
        //获取所有merchantId=0的，即平台商的模板
        result.getData().put("result",shippingService.findShipTemplate(pageNo, pageSize,0));
        return result;
    }

    @GetMapping("findMerchants")
    public OperaResult findShipTemplateMerchants(@Param("pageNo") Integer pageNo, @Param("pageSize")Integer pageSize, OperaResult result){
        //获取所有merchantId不为0的，即非平台商的模板
        result.getData().put("result",shippingService.findShipTemplate(pageNo, pageSize,1));
        return result;
    }

    @GetMapping("findById")
    public OperaResult findShipTemplateById(@Param("id") Integer id, OperaResult result){
        result.getData().put("result",shippingService.findShipTemplateById(id));
        return result;
    }

    @GetMapping("findIdList")
    public OperaResult findShipTemplateByIdList(@RequestParam("idList") List<Integer> idList, OperaResult result){
        log.info("findIdList 入参: {}", JSON.toJSONString(idList));
        result.getData().put("result",shippingService.findByIdList(idList));
        return result;
    }

    @PutMapping("update")
    public OperaResult updateShipTemplate(@RequestBody ShipTemplateBean bean, OperaResult result){
        int num = shippingService.updateShipTemplate(bean);
        if(num == 0){
            result.setCode(501);
            result.setMsg("更新失败");
        }else{
            result.getData().put("result", num);
        }
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

    @GetMapping("findByMpu")
    public OperaResult findShipTemplateByMpu(String mpu, OperaResult result){
        result.getData().put("result",shippingService.findShipTemplateByMpu(mpu));
        return result;
    }
}
