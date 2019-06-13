package com.fengchao.order.controller;

import com.fengchao.order.bean.AddressQueryBean;
import com.fengchao.order.bean.GPSQueryBean;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/address", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AddressController {

    @Autowired
    private AddressService service;

    @PostMapping("/level")
    private OperaResult findLevel(@RequestBody AddressQueryBean queryBean, OperaResult result) {
        result.getData().put("list", service.findByLevel(queryBean)) ;
        return result;
    }

    @PostMapping("/code")
    private OperaResult findCode(@RequestBody GPSQueryBean queryBean, OperaResult result) {
        result.getData().put("code", service.findCode(queryBean)) ;
        return result;
    }

}
