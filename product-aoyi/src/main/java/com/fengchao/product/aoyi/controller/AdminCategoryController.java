package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.model.AoyiBaseCategoryX;
import com.fengchao.product.aoyi.model.RenterCategory;
import com.fengchao.product.aoyi.service.AdminCategoryService;
import com.fengchao.product.aoyi.service.RenterCategoryService;
import com.fengchao.product.aoyi.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/adminCategory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService service;
    @Autowired
    private RenterCategoryService renterCategoryService ;

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
        PageBean category = service.selectNameList(offset, limit, query);
        result.getData().put("result", category) ;
        return result;
    }

    @GetMapping("all")
    public OperaResult all(OperaResult result){
        List<AoyiBaseCategoryX> category = service.selectAll();
        result.getData().put("result", category) ;
        return result;
    }

    @GetMapping("all/v2")
    public OperaResult allV2(@RequestHeader("renterId") Integer renterId){
        OperaResult result = new OperaResult() ;
        List<AoyiBaseCategoryX> category = service.selectAll();
        result.getData().put("result", category) ;
        return result;
    }

    @GetMapping("/oneLevel")
    private OperaResult findOneLevelList(Integer offset, Integer limit, OperaResult result) {
        if (offset < 0) {
            offset = 1;
        }
        if (limit > 100) {
            result.setCode(200500);
            result.setMsg("limit 不能大于100");
            return result;
        }
        Integer categoryClass = 1;
        result.getData().put("result", service.selectLimit(offset, limit, categoryClass)) ;
        return result ;
    }

    @GetMapping("/subcategory")
    private OperaResult findSubLevelList(Integer offset, Integer limit, Integer parentId, OperaResult result) {
        if (offset < 0) {
            offset = 1;
        }
        if (limit > 100) {
            result.setCode(200500);
            result.setMsg("limit 不能大于100");
            return result;
        }
        result.getData().put("result", service.selectSubLevelList(offset, limit, parentId)) ;
        return result ;
    }

    @PostMapping("/saveCategory")
    private OperaResult saveCategory(@RequestBody AoyiBaseCategoryX bean, OperaResult result) {
        result.getData().put("categoryId", service.insertSelective(bean));
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
        List<AoyiBaseCategoryX> categorys = service.selectCategoryList(id, includeSub);
        result.getData().put("result", categorys) ;
        return result ;
    }

    @PostMapping("/categoryList")
    private OperaResult selectByCategoryIdList(@RequestBody List<String> categories, OperaResult result) {
        List<CategoryQueryBean> categoryBeans = service.selectByCategoryIdList(categories);
        result.getData().put("result", categoryBeans) ;
        return result ;
    }

    /**
     * 根据id集合查询品类列表
     *
     * @param categoryIdList
     * @return
     */
    @GetMapping("/category/listByIds")
    public OperaResponse<List<CategoryQueryBean>> queryCategorysByCategoryIdList(@RequestParam("categoryIdList") List<Integer> categoryIdList,
                                                      OperaResponse<List<CategoryQueryBean>> operaResponse) {
        log.info("根据id集合查询品类列表 入参:{}", JSONUtil.toJsonString(categoryIdList));

        List<CategoryQueryBean> categoryQueryBeanList = new ArrayList<>();
        try {
            categoryQueryBeanList = service.queryCategorysByCategoryIdList(categoryIdList);

            operaResponse.setData(categoryQueryBeanList);
        } catch (Exception e) {
            log.error("根据id集合查询品类列表 异常:{}", e.getMessage(), e);

            operaResponse.setCode(500);
            operaResponse.setMsg("根据id集合查询品类列表异常");
            operaResponse.setData(null);
        }

        log.info("根据id集合查询品类列表 返回:{}", JSONUtil.toJsonString(categoryQueryBeanList));

        return operaResponse;
    }

    @PostMapping("/renter")
    private OperaResponse saveRenterCategory(@RequestBody List<RenterCategory> bean) {
        return renterCategoryService.addBatch(bean) ;
    }

    @PutMapping("/renter")
    private OperaResponse updateRenterCategory(@RequestBody List<RenterCategory> bean) {
        return renterCategoryService.updateBatch(bean) ;
    }

    @DeleteMapping("/renter")
    private OperaResponse deleteRenterCategory(Integer id) {
        OperaResponse response = new OperaResponse() ;
        renterCategoryService.delete(id);
        return  response;
    }

    @PostMapping("/renter/all")
    private OperaResponse findRenterCategorys(@RequestBody RenterCategoryQueryBean bean) {
        OperaResponse response = new OperaResponse() ;
        response.setData(renterCategoryService.findListByRenterId(bean) );
        return response;
    }

    @PutMapping("/renter/batch")
    private OperaResponse deleteRenterCategoryBatch(@RequestBody List<Integer> ids) {
        OperaResponse response = new OperaResponse() ;
        renterCategoryService.deleteBatch(ids);
        return  response;
    }
}
