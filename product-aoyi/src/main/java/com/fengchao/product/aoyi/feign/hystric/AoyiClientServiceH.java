package com.fengchao.product.aoyi.feign.hystric;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengchao.product.aoyi.bean.*;
import com.fengchao.product.aoyi.feign.AoyiClientService;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiItemDetailResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.AoyiQueryInventoryResDto;
import com.fengchao.product.aoyi.rpc.extmodel.weipinhui.CategoryResDto;

import java.util.List;

@Deprecated
public class AoyiClientServiceH implements AoyiClientService {
    @Override
    public OperaResponse price(QueryCityPrice queryBean) {
        OperaResponse result = new OperaResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(queryBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("价格服务失败 " + msg);
        return result;
    }

    @Override
    public OperaResponse inventory(QueryInventory queryBean) {
        OperaResponse result = new OperaResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(queryBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("价格服务失败" + msg);
        return result;
    }

    @Override
    public OperaResponse shipCarriage(QueryCarriage queryBean) {
        OperaResponse result = new OperaResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = "";
        try {
            msg = objectMapper.writeValueAsString(queryBean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        result.setCode(404);
        result.setMsg("价格服务失败" + msg);
        return result;
    }

    @Override
    public OperaResponse priceGAT(QueryCityPrice queryBean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse getSpuIdList(QueryBean queryBean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse getSpuDetail(String spuIds) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse getSkuDetail(String skuId) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse findBrandList(QueryBean queryBean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse findProdCategory(String categoryId) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse findSkuSalePrice(String codes) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse findSkuInverntory(StarInventoryRequestBean inventoryRequestBean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse weipinhuiGetBrand(Integer pageNumber, Integer pageSize) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse<List<CategoryResDto>> weipinhuiGetCategory(Integer pageNumber, Integer pageSize) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse<List<AoyiItemDetailResDto>> weipinhuiQueryItemsList(Integer pageNumber, Integer pageSize) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse<AoyiItemDetailResDto> weipinhuiQueryItemDetial(String itemId) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse<AoyiQueryInventoryResDto> weipinhuiQueryItemInventory(String itemId, String skuId, Integer num, String divisionCode) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse<JSONArray> kiosk() {
        return HystrixDefaultFallback.defaultReponseFallback();
    }

    @Override
    public OperaResponse<JSONArray> kioskSlotStatus(String status) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }
}
