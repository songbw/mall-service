package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.exception.ProductException;
import com.fengchao.product.aoyi.model.AoyiBaseBrand;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexX;
import com.fengchao.product.aoyi.service.BrandService;
import com.fengchao.product.aoyi.service.CategoryService;
import com.fengchao.product.aoyi.service.ThirdProdService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/third/prod", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class ThirdProdController {

    @Autowired
    private ThirdProdService service;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService ;

    /**
     * 新增商品
     *
     * @param bean
     * @return
     * @throws ProductException
     */
    @PostMapping
    public OperaResult create(@RequestBody AoyiProdIndexX bean){
        log.info("新增商品 入参 AoyiProdIndexX:{}", JSONUtil.toJsonString(bean));
        return service.add(bean);
    }

    @PutMapping
    public OperaResult update(@RequestBody AoyiProdIndexX bean){
        log.info("修改商品 入参 AoyiProdIndexX:{}", JSONUtil.toJsonString(bean));
        return service.update(bean);
    }

    @PutMapping("receive")
    public OperaResult insertOrUpdateByMup(@RequestBody AoyiProdIndex bean){
        log.info("根据MPU添加或修改商品 入参 AoyiProdIndexX:{}", JSONUtil.toJsonString(bean));
        return service.insertOrUpdateByMpu(bean);
    }

    @PutMapping("category/receive")
    public OperaResponse insertCategory(@RequestBody AoyiBaseCategory bean){
        log.info("根据ID添加或修改类目 入参 category:{}", JSONUtil.toJsonString(bean));
        return categoryService.insertOrUpdate(bean);
    }

    @PutMapping("brand/receive")
    public OperaResponse insertBrand(@RequestBody AoyiBaseBrand bean){
        log.info("根据ID添加或修改品牌 入参 AoyiBaseBrand:{}", JSONUtil.toJsonString(bean));
        return brandService.insertOrUpdate(bean);
    }

    @PutMapping("sync")
    public OperaResponse sync(@RequestBody ThirdSyncBean bean){
        log.info("同步商品 入参 AoyiProdIndexX:{}", JSONUtil.toJsonString(bean));
        return service.sync(bean);
    }

    @PutMapping("category/sync")
    public OperaResponse categorySync(@RequestBody CategorySyncBean bean){
        log.info("同步类目 入参 bean:{}", JSONUtil.toJsonString(bean));
        return service.syncCategory(bean);
    }

    @PutMapping("brand/sync")
    public OperaResponse brandSync(@RequestBody ThirdSyncBean bean){
        log.info("同步类目 入参 bean:{}", JSONUtil.toJsonString(bean));
        return service.syncBrand(bean);
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
        if (StringUtils.isEmpty(bean.getSPrice())) {
            result.setCode(210002);
            result.setMsg("sprice 不能为null");
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
