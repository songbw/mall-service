package com.fengchao.aoyi.client.controller;

import com.fengchao.aoyi.client.bean.OperaResult;
import com.fengchao.aoyi.client.bean.QueryCarriage;
import com.fengchao.aoyi.client.bean.QueryCityPrice;
import com.fengchao.aoyi.client.bean.QueryInventory;
import com.fengchao.aoyi.client.bean.dto.BrandResDto;
import com.fengchao.aoyi.client.exception.AoyiClientException;
import com.fengchao.aoyi.client.service.ProductService;
import com.fengchao.aoyi.client.weipinhuiService.ProductWeipinhuiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/weipinhui", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WeipinhuiController {

    private ProductWeipinhuiService productWeipinhuiService;

    @Autowired
    public WeipinhuiController(ProductWeipinhuiService productWeipinhuiService) {
        this.productWeipinhuiService = productWeipinhuiService;
    }

    /**
     * 获取品牌列表
     *
     * http://localhost:8001/weipinhui/getBrand?pageNumber=1&pageSize=20
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GetMapping("/getBrand")
    private OperaResult<List<BrandResDto>> getBrand(@RequestParam("pageNumber") Integer pageNumber,
                                                    @RequestParam("pageSize") Integer pageSize) {
        log.info("获取品牌列表 入参 pageNumber:{}, pageSize:{}", pageNumber, pageSize);

        OperaResult<List<BrandResDto>> operaResult = new OperaResult<>();
        try {
            List<BrandResDto> brandResDtoList = productWeipinhuiService.getBrand(pageNumber, pageSize);

            operaResult.setData(brandResDtoList);
        } catch (Exception e) {
            log.error("获取品牌列表 异常:{}", e.getMessage(), e);
        }

        return operaResult;
    }



}