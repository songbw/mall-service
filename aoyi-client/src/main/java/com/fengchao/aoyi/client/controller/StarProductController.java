package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.QueryBean;
import com.fengchao.aoyi.client.exception.AoyiClientException;
import com.fengchao.aoyi.client.startService.ProductStarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songbw
 * @date 2020/1/13 18:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/star/product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StarProductController {

    @Autowired
    private ProductStarService service ;

    @PostMapping("/spus")
    private OperaResponse getSpuIdList(@RequestBody QueryBean queryBean) throws AoyiClientException {
        return service.getSpuIdList(queryBean);
    }
}
