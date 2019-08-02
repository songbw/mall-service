package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.bean.QueryProdBean;
import com.fengchao.product.aoyi.bean.SerachBean;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.service.AdminProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/adminProd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminProdController {

    @Autowired
    private AdminProdService prodService;

    @GetMapping("prodList")
    public OperaResult findProdList(Integer offset, Integer limit, String state, @RequestHeader("merchant") Integer merchantId, OperaResult result) {
        if (offset < 0) {
            offset = 1;
        }
        if (limit > 100) {
            result.setCode(200500);
            result.setMsg("limit 不能大于100");
            return result;
        }
        PageBean prod = prodService.findProdList(offset, limit, state, merchantId);
        result.getData().put("result", prod);
        return result;
    }

    @PostMapping("search")
    public OperaResult searchProd(@Valid @RequestBody SerachBean bean, @RequestHeader("merchant") Integer merchantHeader, OperaResult result) {
        bean.setMerchantHeader(merchantHeader);
        PageBean pageBean = prodService.selectNameList(bean);
        result.getData().put("result", pageBean);
        return result;
    }

    @PostMapping
    public OperaResult create(@RequestBody AoyiProdIndexX bean, @RequestHeader("merchant") Integer merchantId, OperaResult result) throws ProductException {
//        bean.setMerchantId(merchantId);
        int id = prodService.add(bean);
        result.getData().put("result", id);
        return result;
    }

    @PutMapping
    public OperaResult update(@RequestBody AoyiProdIndexX bean, @RequestHeader("merchant") Integer merchantId, OperaResult result) throws ProductException {
//        bean.setMerchantId(merchantId);
        int id = prodService.update(bean);
        result.getData().put("result", id);
        return result;
    }

    @DeleteMapping
    public OperaResult delete(@RequestHeader("merchant") Integer merchantId, Integer id,  OperaResult result) throws ProductException {
        prodService.delete(merchantId, id);
        return result;
    }


    @GetMapping("prodToRedis")
    public OperaResult getProdListToRedis(OperaResult result) {
        int prodList = prodService.getProdListToRedis();
        result.getData().put("result", prodList);
        return result;
    }

    @PostMapping("prodAll")
    public OperaResult findProdAll(@Valid @RequestBody QueryProdBean bean, OperaResult result) {
        PageBean pageBean = prodService.findProdAll(bean);
        result.getData().put("result", pageBean);
        return result;
    }
}
