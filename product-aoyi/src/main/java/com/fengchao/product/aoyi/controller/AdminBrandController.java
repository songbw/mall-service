package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.model.AoyiBaseBrand;
import com.fengchao.product.aoyi.service.AdminBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/adminBrand", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminBrandController {

    @Autowired
    private AdminBrandService brandService;

    @GetMapping("brandList")
    public OperaResult findBrandList(Integer offset, Integer limit, OperaResult result){
        if (offset < 0) {
            offset = 1;
        }
        if (limit > 100) {
            result.setCode(200500);
            result.setMsg("limit 不能大于100");
            return result;
        }
        PageBean brand = brandService.findBrandList(offset,limit);
        result.getData().put("result",brand);
        return result;
    }

    @PostMapping("updateBrand")
    public OperaResult updateBrand(@RequestBody AoyiBaseBrand bean, OperaResult result){
        result.getData().put("result",brandService.updateBrandbyId(bean));
        return result;
    }

    @PostMapping
    public OperaResult create(@RequestBody AoyiBaseBrand bean, OperaResult result){
        result.getData().put("result",brandService.create(bean));
        return result;
    }

    @DeleteMapping
    public OperaResult delete(Integer id, OperaResult result){
        brandService.delete(id);
        return result;
    }

    @GetMapping("search")
    public OperaResult search(Integer offset, Integer limit, String query, OperaResult result){
        if (offset < 0) {
            offset = 1;
        }
        if (limit > 100) {
            result.setCode(200500);
            result.setMsg("limit 不能大于100");
            return result;
        }
        PageBean  category = brandService.selectNameList(offset,limit,query);
        result.getData().put("result", category) ;
        return result;
    }
}
