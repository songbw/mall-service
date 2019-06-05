package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.bean.SerachBean;
import com.fengchao.product.aoyi.service.AdminProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminProd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminProdController {

    @Autowired
    private AdminProdService prodService;

    @GetMapping("prodList")
    public OperaResult findProdList(Integer offset, Integer limit, String state, OperaResult result) {
        PageBean prod = prodService.findProdList(offset, limit, state);
        result.getData().put("result", prod);
        return result;
    }

    @PostMapping("search")
    public OperaResult searchProd(@RequestBody SerachBean bean, OperaResult result) {
        PageBean pageBean = prodService.selectNameList(bean);
        result.getData().put("result", pageBean);
        return result;
    }

    @GetMapping("prodToRedis")
    public OperaResult getProdListToRedis(OperaResult result) {
        int prodList = prodService.getProdListToRedis();
        result.getData().put("result", prodList);
        return result;
    }
}
