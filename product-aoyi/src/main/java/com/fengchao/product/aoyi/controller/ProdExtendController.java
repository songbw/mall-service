package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.model.ProdExtendX;
import com.fengchao.product.aoyi.service.ProdExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/prodExtend", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProdExtendController {

    @Autowired
    private ProdExtendService service;

    @PostMapping("update")
    public OperaResult updateProdExtend(@RequestBody ProdExtendX bean, @RequestHeader("merchant") Integer merchantId, OperaResult result) {
        result.getData().put("result", service.updateProdExtend(bean));
        return result;
    }

    @DeleteMapping("delete")
    public OperaResult deleteProdExtend(@RequestHeader("merchant") Integer merchantId, String skuid, OperaResult result) {
        result.getData().put("result", service.deleteProdExtend(skuid));
        return result;
    }

}
