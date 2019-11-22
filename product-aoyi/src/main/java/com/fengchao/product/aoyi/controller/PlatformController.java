package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.QueryBean;
import com.fengchao.product.aoyi.model.Platform;
import com.fengchao.product.aoyi.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author songbw
 * @date 2019/11/22 10:54
 */
@RestController
@RequestMapping(value = "/platform", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PlatformController {

    @Autowired
    private PlatformService service ;
    @PostMapping("/all")
    private OperaResponse findList(@RequestBody QueryBean queryBean) {
        return service.findList(queryBean);
    }

    @GetMapping
    private OperaResponse find(Integer id) {
        return service.find(id);
    }

    @PostMapping
    private OperaResponse add(@RequestBody Platform bean) {
        return service.add(bean);
    }

    @DeleteMapping
    private OperaResponse delete(Integer id) {
        return service.delete(id);
    }

    @PutMapping
    private OperaResponse modify(@RequestBody Platform bean) {
        return service.modify(bean);
    }

}
