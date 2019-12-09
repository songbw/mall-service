package com.fengchao.equity.controller;

import com.fengchao.equity.bean.OperaResponse;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.PromotionTypeResDto;
import com.fengchao.equity.bean.QueryProdBean;
import com.fengchao.equity.model.PromotionType;
import com.fengchao.equity.service.PromotionTypeService;
import com.fengchao.equity.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/promotion/type", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class PromotionTypeController {

    @Autowired
    private PromotionTypeService service;

    @PostMapping("findPage")
    public OperaResult findPromotionTypesByPage(QueryProdBean params){
        OperaResult result = new OperaResult();
        result.getData().put("result", service.getPromotionTypes(params.getPageNo(), params.getPageSize(), params.getAppId()));
        return result;
    }


    @GetMapping("queryAllPromotionTypes")
    public OperaResponse queryAllPromotionTypeList(OperaResponse operaResponse) {
        log.info("查询所有活动类型列表 入参:无");

        try {
            // 查询
            List<PromotionTypeResDto> promotionTypeResDtoList = service.queryAllPromotionType();

            operaResponse.setData(promotionTypeResDtoList);
        } catch (Exception e) {
            log.error("查询所有活动类型列表 异常:{}", e.getMessage(), e);

            operaResponse.setData(null);
            operaResponse.setCode(500);
            operaResponse.setMsg("查询所有活动类型列表 异常");
        }

        log.info("查询所有活动类型列表 返回:{}", JSONUtil.toJsonString(operaResponse));
        return operaResponse;
    }
}
