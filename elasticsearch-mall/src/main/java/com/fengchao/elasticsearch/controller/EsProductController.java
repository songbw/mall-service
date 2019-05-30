package com.fengchao.elasticsearch.controller;

import com.fengchao.elasticsearch.domain.CommonResult;
import com.fengchao.elasticsearch.domain.EsProduct;
import com.fengchao.elasticsearch.domain.EsProductRelatedInfo;
import com.fengchao.elasticsearch.service.EsProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 搜索商品管理Controller
 * Created by song on 2019/5/16.
 */
@Controller
@RequestMapping("/esProduct")
public class EsProductController {
    @Autowired
    private EsProductService esProductService;

    @RequestMapping(value = "/importAll", method = RequestMethod.POST)
    @ResponseBody
    public Object importAllList() {
        int count = esProductService.importAll();
        return new CommonResult().success(count);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable Long id) {
        esProductService.delete(id);
        return new CommonResult().success(null);
    }

    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(@RequestParam("ids") List<Long> ids) {
        esProductService.delete(ids);
        return new CommonResult().success(null);
    }

    @RequestMapping(value = "/create/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@PathVariable Long id) {
        EsProduct esProduct = esProductService.create(id);
        if (esProduct != null) {
            return new CommonResult().success(esProduct);
        } else {
            return new CommonResult().failed();
        }
    }

    @RequestMapping(value = "/search/simple", method = RequestMethod.GET)
    @ResponseBody
    public Object search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                         @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<EsProduct> esProductPage = esProductService.search(keyword, pageNum, pageSize);
        return new CommonResult().pageSuccess(esProductPage);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Long brandId,
                         @RequestParam(required = false) Long productCategoryId,
                         @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                         @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                         @RequestParam(required = false, defaultValue = "0") Integer sort) {
        Page<EsProduct> esProductPage = esProductService.search(keyword, brandId, productCategoryId, pageNum, pageSize, sort);
        return new CommonResult().pageSuccess(esProductPage);
    }

    @RequestMapping(value = "/recommend/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Object recommend(@PathVariable Long id,
                            @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                            @RequestParam(required = false, defaultValue = "5") Integer pageSize){
        Page<EsProduct> esProductPage = esProductService.recommend(id, pageNum, pageSize);
        return new CommonResult().pageSuccess(esProductPage);
    }

    @RequestMapping(value = "/search/relate",method = RequestMethod.GET)
    @ResponseBody
    public Object searchRelatedInfo(@RequestParam(required = false) String keyword){
        EsProductRelatedInfo productRelatedInfo = esProductService.searchRelatedInfo(keyword);
        return new CommonResult().success(productRelatedInfo);
    }
}
