package com.fengchao.order.controller;

import com.fengchao.order.bean.InvoiceInfoQueryBean;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.model.InvoiceInfo;
import com.fengchao.order.service.InvoiceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 发票信息接口
 */
@RestController
@RequestMapping(value = "/invoice", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class InvoiceInfoController {

    @Autowired
    private InvoiceInfoService service;

    @PostMapping
    private OperaResult add(@RequestBody InvoiceInfo bean, OperaResult result) {
        if (StringUtils.isEmpty(bean.getUserId())) {
            result.setCode(4000001);
            result.setMsg("userId 不能为空");
            return result;
        }
        InvoiceInfo invoiceInfo = service.findByUserId(bean.getUserId()) ;
        if (invoiceInfo != null) {
            result.setCode(4000002);
            result.setMsg("用户重复。");
            return result;
        }
        result.getData().put("result", service.add(bean)) ;
        return result;
    }

    @DeleteMapping
    private OperaResult delete(Integer id, OperaResult result) {
        result.getData().put("result", service.delete(id)) ;
        return result;
    }

    @PutMapping
    private OperaResult modify(@RequestBody InvoiceInfo bean, OperaResult result) {
        result.getData().put("result", service.modify(bean)) ;
        return result;
    }

    @GetMapping
    private OperaResult find(Integer id, OperaResult result) {
        result.getData().put("result", service.find(id)) ;
        return result;
    }

    @GetMapping("user")
    private OperaResult findByUserId(String userId, OperaResult result) {
        result.getData().put("result", service.findByUserId(userId)) ;
        return result;
    }

}
