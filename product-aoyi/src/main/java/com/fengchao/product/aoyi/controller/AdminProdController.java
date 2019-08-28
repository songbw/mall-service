package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.service.AdminProdService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/adminProd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
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

    /**
     * 搜索商品
     *
     * @param bean
     * @param merchantHeader
     * @param result
     * @return
     */
    @PostMapping("search")
    public OperaResult searchProd(@Valid @RequestBody SerachBean bean, @RequestHeader("merchant") Integer merchantHeader,
                                  OperaResult result) {
        log.info("搜索商品 入参 bean:{}, merchantId:{}", JSONUtil.toJsonString(bean), merchantHeader);

        bean.setMerchantHeader(merchantHeader);
         PageBean pageBean = prodService.selectNameList(bean);
//        PageBean pageBean = prodService.selectProductListPageable(bean);
        result.getData().put("result", pageBean);

        log.info("搜索商品 返回:{}", JSONUtil.toJsonString(result));

        return result;
    }

    /**
     * 创建商品
     *
     * @param bean
     * @param merchantId
     * @param result
     * @return
     * @throws ProductException
     */
    @PostMapping
    public OperaResult create(@RequestBody AoyiProdIndexX bean, @RequestHeader("merchant") Integer merchantId,
                                OperaResult result) throws ProductException {
        log.info("创建商品 入参 AoyiProdIndexX:{}, merchantId:{}", JSONUtil.toJsonString(bean), merchantId);

        try {
            // 入参校验
            if (bean.getMerchantId() <= 0) {
                throw new Exception("参数merchantId不合法");
            }

            if (StringUtils.isBlank(bean.getPrice()) || Float.valueOf(bean.getPrice()) <= 0) {
                throw new Exception("参数price不合法");
            }

            // 执行新增商品
            int id = prodService.add(bean);

            result.getData().put("result", id);
        } catch (Exception e) {
            log.error("创建商品 异常:{}", e.getMessage(), e);

            result.setCode(500);
            result.setMsg(e.getMessage());
        }

        log.info("创建商品 返回:{}", JSONUtil.toJsonString(result));

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
