package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.OperaResult;
import com.fengchao.aoyi.client.exception.AoyiClientException;
import com.fengchao.aoyi.client.service.FusionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songbw
 * @date 2021/1/27 17:58
 */
@RestController
@RequestMapping(value = "/fusion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FusionController {

    private final FusionService service ;

    @Autowired
    public FusionController(FusionService service) {
        this.service = service;
    }

    @GetMapping("/kiosk")
    private OperaResponse kiosk() {
        return service.getAllKiosk();
    }

    @GetMapping("/kiosk/slot/status")
    private OperaResponse kiosk(String status) {
        return service.getAllSoltStatus(status);
    }
}
