package com.fengchao.order.controller;

import com.fengchao.order.bean.KuaidiCodeQueryBean;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.model.KuaidiCode;
import com.fengchao.order.service.KuaidiCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/kuaidi", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KuaidiCodeController {

    @Autowired
    private KuaidiCodeService service;

    @PostMapping("/all")
    private OperaResult find(@RequestBody KuaidiCodeQueryBean queryBean, OperaResult result) {
        result.getData().put("result", service.findList(queryBean)) ;
        return result;
    }

    @PostMapping
    private OperaResult add(@RequestBody KuaidiCode bean, OperaResult result) {
        result.getData().put("result", service.add(bean)) ;
        return result;
    }

    @DeleteMapping
    private OperaResult delete(Integer id, OperaResult result) {
        result.getData().put("result", service.delete(id)) ;
        return result;
    }

    @PutMapping("/num")
    private OperaResult modifyNum(@RequestBody KuaidiCode bean, OperaResult result) {
        result.getData().put("result", service.modify(bean)) ;
        return result;
    }

}
