package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.QueryBean;
import com.fengchao.product.aoyi.service.BrandService;
import com.fengchao.product.aoyi.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/platform", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PlatformController {

    @Autowired
    private PlatformService service;

    @GetMapping
    private OperaResponse find(String appId) {
        OperaResponse response = new OperaResponse() ;
        if (StringUtils.isEmpty(appId)) {
            response.setCode(2000001);
            response.setMsg("appId不能为空。");
            return response ;
        }
        response.setData(service.findByAppId(appId));
        return response;
    }

    @PostMapping("/all")
    private OperaResponse findList(@RequestBody QueryBean queryBean) {
        OperaResponse response = new OperaResponse();
        response.setData(service.findList(queryBean));
        return response;
    }

    @GetMapping("sub")
    private OperaResponse findSub(String appId) {
        OperaResponse response = new OperaResponse() ;
        if (StringUtils.isEmpty(appId)) {
            response.setCode(2000001);
            response.setMsg("appId不能为空。");
            return response ;
        }
        response.setData(service.findByAppId(appId));
        return response;
    }
}
