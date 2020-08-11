package com.fengchao.aoyi.client.startService.impl;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.QueryBean;
import com.fengchao.aoyi.client.config.StarClientConfig;
import com.fengchao.aoyi.client.starBean.*;
import com.fengchao.aoyi.client.startService.ProductStarService;
import com.fengchao.aoyi.client.utils.JSONUtil;
import com.fengchao.aoyi.client.utils.StarHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author songbw
 * @date 2020/1/2 15:33
 */
@EnableConfigurationProperties({StarClientConfig.class})
@Slf4j
@Service
public class ProductStarServiceImpl implements ProductStarService {

    @Autowired
    private StarClientConfig starClientConfig;

    @Override
    public OperaResponse getSpuIdList(QueryBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("pageIndex", bean.getPageNo() + "");
        params.put("pageSize", bean.getPageSize() + "");
        if (!StringUtils.isEmpty(bean.getStartTime())) {
            params.put("createStartTime", bean.getStartTime()) ;
        }
        if (!StringUtils.isEmpty(bean.getEndTime())) {
            params.put("createEndTime", bean.getEndTime()) ;
        }
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_GOOD_SPU_LIST, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("同步商品获取商品SpuId列表, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse getSpuDetail(String spuIds) {
        Map<String, String> params = new HashMap<>();
        params.put("spuIds", spuIds);
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_GOOD_SPU_DETAIL, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("根据SPUID 查询SPU详情, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse getSkuListDetailBySpuId(String spuId) {
        Map<String, String> params = new HashMap<>();
        params.put("spuId", spuId);
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_GOOD_SKU_DETAIL, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("根据SPUID 查询SKU列表, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse findBrandList(QueryBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("pageIndex", bean.getPageNo() + "");
        params.put("pageSize", bean.getPageSize() + "");
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_GOOD_BRAND, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("查询品牌列表, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse findProdCategory(String categoryId) {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(categoryId)) {
            params.put("categoryId", categoryId);
        }
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_GOOD_CATEGORY, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("查询分类信息, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse findSkuInventory(InventoryQueryBean bean) {
        Map<String, String> params = new HashMap<>();
//        params.put("areaId", "4524130,4524157,4524163");
//        params.put("skuIds", "5769");
        params.put("codes", bean.getCodes());
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_GOOD_FIND_SKU_INVENTORY, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("查询商品库存, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse findSkuSalePrice(String codes) {
        Map<String, String> params = new HashMap<>();
//        params.put("skuIds", "5940");
        params.put("codes", codes);
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_GOOD_FIND_SKU_PRICE, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("查询商品价格, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse getAddressInfo(AddressInfoQueryBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("provinceName", bean.getProvinceName());
        params.put("cityName", bean.getCityName());
        params.put("regionName", bean.getRegionName());
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_ADDRESS_INFO, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("查询地址信息, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse preHoldSkuInventory(HoldSkuInventoryQueryBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("areaId", bean.getAreaId());
        params.put("outOrderNo", bean.getOutOrderNo());
//        params.put("skuInvList", "[{\"spuId\":\"59587\",\"skuId\":\"6072\",\"count\":\"10000\"}]");
        params.put("codeInvList", JSONUtil.toJsonString(bean.getCodeInvList()));
        params.put("regionId", bean.getRegionId()) ;
        log.info("预占商品库存, 入参：{}", JSONUtil.toJsonString(params));
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_GOOD_HOLD_SKU_INVENTORY, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("预占商品库存, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }

    @Override
    public OperaResponse releaseSkuInventory(ReleaseSkuInventoryQueryBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("outOrderNo", bean.getOutOrderNo());
//        params.put("skuInvList", "[{\"spuId\":\"115332\",\"skuId\":\"85775\"}]");
        params.put("codeInvList", JSONUtil.toJsonString(bean.getCodeInvList()));
        log.info("释放预占商品库存, 入参：{}", JSONUtil.toJsonString(params));
        OperaResponse response = StarHttpClient.post(params,OperaResponse.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_GOOD_RELEASE_SKU_INVENTORY, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("释放预占商品库存, 返回结果：{}", JSONUtil.toJsonString(response));
        if (response.getCode() == 0) {
            response.setCode(200);
        } else {
            response.setCode(response.getCode());
            response.setMsg(response.getMessage());
        }
        return response;
    }
}
