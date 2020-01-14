package com.fengchao.aoyi.client.weipinhuiService.impl;

import com.fengchao.aoyi.client.bean.dto.BrandResDto;
import com.fengchao.aoyi.client.bean.dto.CategoryResDto;
import com.fengchao.aoyi.client.bean.dto.WeipinhuiResponse;
import com.fengchao.aoyi.client.utils.JSONUtil;
import com.fengchao.aoyi.client.weipinhuiService.ProductWeipinhuiService;
import com.fengchao.aoyi.client.weipinhuiService.client.WeipinhuiServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductWeipinhuiServiceImpl implements ProductWeipinhuiService {

    private WeipinhuiServiceClient weipinhuiServiceClient;

    @Autowired
    public ProductWeipinhuiServiceImpl(WeipinhuiServiceClient weipinhuiServiceClient) {
        this.weipinhuiServiceClient = weipinhuiServiceClient;
    }

    @Override
    public List<BrandResDto> getBrand(Integer pageNumber, Integer pageSize) throws Exception {
        try {
            // 1. 执行请求
            WeipinhuiResponse weipinhuiResponse = weipinhuiServiceClient.getBrand(pageNumber, pageSize);

            log.info("获取品牌列表 返回WeipinhuiResponse:{}", JSONUtil.toJsonString(weipinhuiResponse));

            // 2. 解析返回
            List<BrandResDto> brandResDtoList =
                    JSONUtil.parseList(weipinhuiResponse.getResult().toString(), BrandResDto.class);

            log.info("获取品牌列表 结果:{}", JSONUtil.toJsonString(brandResDtoList));

            return brandResDtoList;
        } catch (Exception e) {
            log.error("获取品牌列表 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public List<CategoryResDto> getCategory(Integer pageNumber, Integer pageSize) throws Exception {
        try {
            // 1. 执行请求
            WeipinhuiResponse weipinhuiResponse = weipinhuiServiceClient.getCategory(pageNumber, pageSize);

            log.info("获取类目列表 返回WeipinhuiResponse:{}", JSONUtil.toJsonString(weipinhuiResponse));

            // 2. 解析返回
            List<CategoryResDto> categoryResDtoList =
                    JSONUtil.parseList(weipinhuiResponse.getResult().toString(), CategoryResDto.class);

            log.info("获取类目列表 结果:{}", JSONUtil.toJsonString(categoryResDtoList));

            return categoryResDtoList;
        } catch (Exception e) {
            log.error("获取类目列表 异常:{}", e.getMessage(), e);

            throw e;
        }
    }
}
