package com.fengchao.aoyi.client.weipinhuiService.impl;

import com.fengchao.aoyi.client.bean.dto.weipinhui.req.AoyiConfirmOrderRequest;
import com.fengchao.aoyi.client.bean.dto.weipinhui.req.AoyiOrderLogisticsRequest;
import com.fengchao.aoyi.client.bean.dto.weipinhui.req.AoyiReleaseOrderRequest;
import com.fengchao.aoyi.client.bean.dto.weipinhui.req.AoyiRenderOrderRequest;
import com.fengchao.aoyi.client.bean.dto.weipinhui.res.*;
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
                    JSONUtil.parseList(weipinhuiResponse.getResult() == null ? null : weipinhuiResponse.getResult().toString(), BrandResDto.class);

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
                    JSONUtil.parseList(weipinhuiResponse.getResult() == null ? null : weipinhuiResponse.getResult().toString(), CategoryResDto.class);

            log.info("获取类目列表 结果:{}", JSONUtil.toJsonString(categoryResDtoList));

            return categoryResDtoList;
        } catch (Exception e) {
            log.error("获取类目列表 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public List<AoyiItemDetailResDto> queryItemsList(Integer pageNumber, Integer pageSize) throws Exception {
        try {
            // 1. 执行请求
            WeipinhuiResponse weipinhuiResponse = weipinhuiServiceClient.queryItemsList(pageNumber, pageSize);

            log.info("获取items列表 返回WeipinhuiResponse:{}", JSONUtil.toJsonString(weipinhuiResponse));

            // 2. 解析返回
            List<AoyiItemDetailResDto> aoyiItemDetailResDtoList =
                    JSONUtil.parseList(weipinhuiResponse.getResult() == null ? null : weipinhuiResponse.getResult().toString(), AoyiItemDetailResDto.class);

            log.info("获取items列表 结果:{}", JSONUtil.toJsonStringWithoutNull(aoyiItemDetailResDtoList));

            return aoyiItemDetailResDtoList;
        } catch (Exception e) {
            log.error("获取items列表 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public AoyiItemDetailResDto queryItemDetial(String itemId) throws Exception {
        try {
            // 1. 执行请求
            WeipinhuiResponse weipinhuiResponse = weipinhuiServiceClient.queryItemDetial(itemId);

            log.info("根据itemId查询详情 返回WeipinhuiResponse:{}", JSONUtil.toJsonString(weipinhuiResponse));

            // 2. 解析返回
            AoyiItemDetailResDto aoyiItemDetailResDto =
                    JSONUtil.parse(weipinhuiResponse.getResult() == null ? null : weipinhuiResponse.getResult().toString(), AoyiItemDetailResDto.class);

            log.info("根据itemId查询详情 结果:{}", JSONUtil.toJsonStringWithoutNull(aoyiItemDetailResDto));

            return aoyiItemDetailResDto;
        } catch (Exception e) {
            log.error("根据itemId查询详情 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public AoyiQueryInventoryResDto queryItemInventory(String itemId, String skuId, Integer num, String divisionCode) throws Exception {
        try {
            // 1. 执行请求
            WeipinhuiResponse weipinhuiResponse = weipinhuiServiceClient.queryItemInventory(itemId, skuId, num, divisionCode);

            log.info("库存查询接口 返回WeipinhuiResponse:{}", JSONUtil.toJsonString(weipinhuiResponse));

            // 2. 解析返回
            AoyiQueryInventoryResDto aoyiQueryInventoryResDto =
                    JSONUtil.parse(weipinhuiResponse.getResult() == null ? null : weipinhuiResponse.getResult().toString(), AoyiQueryInventoryResDto.class);

            log.info("库存查询接口 结果:{}", JSONUtil.toJsonStringWithoutNull(aoyiQueryInventoryResDto));

            return aoyiQueryInventoryResDto;
        } catch (Exception e) {
            log.error("库存查询接口 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public void renderOrder(AoyiRenderOrderRequest aoyiRenderOrderRequest) throws Exception {
        try {
            // 1. 执行请求
            WeipinhuiResponse weipinhuiResponse = weipinhuiServiceClient.renderOrder(aoyiRenderOrderRequest);

            log.info("预占订单接口 返回WeipinhuiResponse:{}", JSONUtil.toJsonString(weipinhuiResponse));
        } catch (Exception e) {
            log.error("预占订单接口 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public void createOrder(AoyiConfirmOrderRequest aoyiConfirmOrderRequest) throws Exception {
        try {
            // 1. 执行请求
            WeipinhuiResponse weipinhuiResponse = weipinhuiServiceClient.createOrder(aoyiConfirmOrderRequest);

            log.info("确认订单接口 返回WeipinhuiResponse:{}", JSONUtil.toJsonString(weipinhuiResponse));
        } catch (Exception e) {
            log.error("确认订单接口 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public void releaseOrder(AoyiReleaseOrderRequest aoyiReleaseOrderRequest) throws Exception {
        try {
            // 1. 执行请求
            WeipinhuiResponse weipinhuiResponse = weipinhuiServiceClient.releaseOrder(aoyiReleaseOrderRequest);

            log.info("取消订单接口 返回WeipinhuiResponse:{}", JSONUtil.toJsonString(weipinhuiResponse));
        } catch (Exception e) {
            log.error("取消订单接口 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public AoyiAdrressResDto queryAddress(Integer pageNumber, Integer pageSize) throws Exception {
        try {
            // 1. 执行请求
            WeipinhuiResponse weipinhuiResponse = weipinhuiServiceClient.queryAddress(pageNumber, pageSize);

            log.info("获取地址接口 返回WeipinhuiResponse:{}", JSONUtil.toJsonString(weipinhuiResponse));

            // 2. 解析返回
            AoyiAdrressResDto aoyiAdrressResDto =
                    JSONUtil.parse(weipinhuiResponse.getResult() == null ? null : weipinhuiResponse.getResult().toString(), AoyiAdrressResDto.class);

            log.info("获取地址接口 结果:{}", JSONUtil.toJsonStringWithoutNull(aoyiAdrressResDto));

            return aoyiAdrressResDto;
        } catch (Exception e) {
            log.error("获取地址接口 异常:{}", e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public AoyiLogisticsResDto queryOrderLogistics(AoyiOrderLogisticsRequest aoyiOrderLogisticsRequest) throws Exception {
        try {
            // 1. 执行请求
            WeipinhuiResponse weipinhuiResponse = weipinhuiServiceClient.queryOrderLogistics(aoyiOrderLogisticsRequest);

            log.info("物流查询接口 返回WeipinhuiResponse:{}", JSONUtil.toJsonString(weipinhuiResponse));

            // 2. 解析返回
            AoyiLogisticsResDto aoyiLogisticsResDto =
                    JSONUtil.parse(weipinhuiResponse.getResult() == null ?
                            null : weipinhuiResponse.getResult().toString(), AoyiLogisticsResDto.class);

            log.info("物流查询接口 结果:{}", JSONUtil.toJsonStringWithoutNull(aoyiLogisticsResDto));

            return aoyiLogisticsResDto;
        } catch (Exception e) {
            log.error("物流查询接口 异常:{}", e.getMessage(), e);

            throw e;
        }
    }
}
