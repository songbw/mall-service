package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.CategoryBean;
import com.fengchao.product.aoyi.bean.CategoryQueryBean;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.model.AoyiBaseCategory;
import com.fengchao.product.aoyi.service.AdminCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/adminCategory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService service;

    @GetMapping("search")
    public OperaResult search(Integer offset, Integer limit, String query, OperaResult result){
        PageBean category = service.selectNameList(offset, limit, query);
        result.getData().put("result", category) ;
        return result;
    }

    @GetMapping("/oneLevel")
    private OperaResult findOneLevelList(Integer offset, Integer limit, OperaResult result) {
        Integer categoryClass = 1;
        result.getData().put("result", service.selectLimit(offset, limit, categoryClass)) ;
        return result ;
    }

    @GetMapping("/subcategory")
    private OperaResult findSubLevelList(Integer offset, Integer limit, Integer parentId, OperaResult result) {
        result.getData().put("result", service.selectSubLevelList(offset, limit, parentId)) ;
        return result ;
    }

    @PostMapping("/saveCategory")
    private OperaResult saveCategory(@RequestBody CategoryBean bean, OperaResult result) {
        service.insertSelective(bean);
        return result ;
    }

    @PostMapping("/updateCategory")
    private OperaResult updateCategory(@RequestBody CategoryBean bean, OperaResult result) {
        service.updateByPrimaryKeySelective(bean);
        return result ;
    }

    @DeleteMapping
    private OperaResult delete(Integer id, OperaResult result) {
        service.delete(id);
        return result ;
    }

    @GetMapping("/category")
    private OperaResult findCategoryList(Integer id, boolean includeSub, OperaResult result) {
        List<AoyiBaseCategory> categorys = service.selectCategoryList(id, includeSub);
        result.getData().put("result", categorys) ;
        return result ;
    }

    @PostMapping("/categoryList")
    private OperaResult selectByCategoryIdList(@RequestBody List<String> categories, OperaResult result) {
        List<CategoryQueryBean> categoryBeans = service.selectByCategoryIdList(categories);
        result.getData().put("result", categoryBeans) ;
        return result ;
    }
}
