package com.fengchao.equity.controller;

import com.fengchao.equity.bean.QueryBean;
import com.fengchao.equity.model.CouponTags;
import com.fengchao.equity.service.CouponTagsService;
import com.fengchao.equity.bean.OperaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/adminCouponTag", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminCouponTagsController {
    @Autowired
    private CouponTagsService couponTagsService;

    @PostMapping("tags")
    public OperaResult createTags(@RequestBody CouponTags bean, OperaResult result){
        couponTagsService.createTags(bean);
        result.getData().put("tagId",bean.getId());
        return result;
    }

    @GetMapping("tags")
    public OperaResult findTags(QueryBean bean, OperaResult result){
        result.getData().put("result", couponTagsService.findTags(bean.getOffset(), bean.getLimit()));
        return result;
    }

    @PutMapping("tags")
    public OperaResult updateTags(@RequestBody CouponTags bean, OperaResult result){
        result.getData().put("result",couponTagsService.updateTags(bean));
        return result;
    }

    @DeleteMapping("tags/{id}")
    public OperaResult deleteTags(@PathVariable("id")Integer id, OperaResult result){
        result.getData().put("result",couponTagsService.deleteTags(id));
        return result;
    }

}
