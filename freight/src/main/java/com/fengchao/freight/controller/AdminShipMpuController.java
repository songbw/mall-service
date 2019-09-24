package com.fengchao.freight.controller;

import com.fengchao.freight.bean.OperaResult;
import com.fengchao.freight.bean.ShipMpuBean;
import com.fengchao.freight.service.ShipMpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminShipMpu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminShipMpuController {

    @Autowired
    private ShipMpuService service;

    @PostMapping("create")
    public OperaResult createShipMpu(@RequestBody ShipMpuBean bean, OperaResult result){
        result.getData().put("id",service.createShipMpu(bean));
        return result;
    }

    @GetMapping("findByMpu")
    public OperaResult findShipMpuById(Integer id, OperaResult result){
        result.getData().put("result",service.findShipMpuById(id));
        return result;
    }

    @PutMapping("update")
    public OperaResult updateShipMpu(@RequestBody ShipMpuBean bean, OperaResult result){
        int num = service.updateShipMpu(bean);
        if(num == 0){
            result.setCode(501);
            result.setMsg("更新失败");
        }else{
            result.getData().put("result", num);
        }
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteShipMpu(Integer id, OperaResult result){
        result.getData().put("result",service.deleteShipMpu(id));
        return result;
    }
}
