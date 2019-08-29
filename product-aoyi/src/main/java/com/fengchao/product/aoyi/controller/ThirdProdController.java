package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.service.AdminProdService;
import com.fengchao.product.aoyi.service.ThirdProdService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/third/prod", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class ThirdProdController {

    @Autowired
    private ThirdProdService service;

    /**
     * 新增商品
     *
     * @param bean
     * @return
     * @throws ProductException
     */
    @PostMapping
    public OperaResult create(@RequestBody AoyiProdIndexX bean){
        OperaResult result = new OperaResult();
        log.info("新增商品 入参 AoyiProdIndexX:{}", JSONUtil.toJsonString(bean));
        try {
            int id = service.add(bean);
            result.getData().put("result", id);
        } catch (Exception e) {
            log.error("新增商品 异常:{}", e.getMessage(), e);
            result.setCode(500);
            result.setMsg(e.getMessage());
        }
        log.info("新增商品 返回:{}", JSONUtil.toJsonString(result));
        return result;
    }

    @PutMapping
    public OperaResult update(@RequestBody AoyiProdIndexX bean, @RequestHeader("merchant") Integer merchantId, OperaResult result) throws ProductException {
//        bean.setMerchantId(merchantId);
        int id = service.update(bean);
        result.getData().put("result", id);
        return result;
    }

    /**
     * 更新产品价格
     * @param bean
     * @return
     * @throws ProductException
     */
    @PutMapping("/price")
    public OperaResponse updatePrice(@RequestBody PriceBean bean) throws ProductException {
        OperaResponse result = new OperaResponse();
        if (bean.getMerchantId() != 2) {
            result.setCode(210001);
            result.setMsg("merchantId 错误");
            return result;
        }
        if (StringUtils.isEmpty(bean.getPrice())) {
            result.setCode(210002);
            result.setMsg("price 不能为null");
            return result;
        }
        if (StringUtils.isEmpty(bean.getSkuId())) {
            result.setCode(210002);
            result.setMsg("skuid 不能为null");
            return result;
        }
        service.updatePrice(bean);
        return result;
    }

    @PutMapping("/state")
    public OperaResponse updateState(@RequestBody StateBean bean) throws ProductException {
        OperaResponse result = new OperaResponse();
        if (bean.getMerchantId() != 2) {
            result.setCode(210001);
            result.setMsg("merchantId 错误");
            return result;
        }
        if (StringUtils.isEmpty(bean.getState())) {
            result.setCode(210002);
            result.setMsg("state 不能为null");
            return result;
        }
        if (StringUtils.isEmpty(bean.getSkuId())) {
            result.setCode(210002);
            result.setMsg("skuid 不能为null");
            return result;
        }
        if ("1".equals(bean.getState()) || "0".equals(bean.getState())) {
            service.updateState(bean);
        } else {
            result.setCode(210002);
            result.setMsg("state 错误");
            return result;
        }
        return result;
    }

    @DeleteMapping
    public OperaResult delete(@RequestHeader("merchant") Integer merchantId, Integer id,  OperaResult result) throws ProductException {
        service.delete(merchantId, id);
        return result;
    }

}
