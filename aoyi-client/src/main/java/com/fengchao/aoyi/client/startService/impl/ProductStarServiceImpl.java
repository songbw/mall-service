package com.fengchao.aoyi.client.startService.impl;

import com.fengchao.aoyi.client.bean.OperaResponse;
import com.fengchao.aoyi.client.bean.QueryBean;
import com.fengchao.aoyi.client.config.StarClientConfig;
import com.fengchao.aoyi.client.starBean.AddressInfoQueryBean;
import com.fengchao.aoyi.client.starBean.HoldSkuInventoryQueryBean;
import com.fengchao.aoyi.client.starBean.InventoryQueryBean;
import com.fengchao.aoyi.client.starBean.ReleaseSkuInventoryQueryBean;
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
        OperaResponse response = new OperaResponse() ;
        String res = StarHttpClient.post(params,String.class, starClientConfig.getBaseUrl(), StarHttpClient.STAR_GOOD_SPU_LIST, starClientConfig.getAppKey(), starClientConfig.getAppSecret()) ;
        log.info("同步商品获取商品SpuId列表, 返回结果：{}", res);
        return response;
    }

    @Override
    public OperaResponse getSpuDetail(String spuIds) {
        return null;
    }

    @Override
    public OperaResponse getSkuListDetailBySpuId(String spuId) {
        return null;
    }

    @Override
    public OperaResponse findBrandList(QueryBean bean) {
        return null;
    }

    @Override
    public OperaResponse findProdCategory(String categoryId) {
        return null;
    }

    @Override
    public OperaResponse findSkuInventory(InventoryQueryBean bean) {
        return null;
    }

    @Override
    public OperaResponse findSkuSalePrice(String codes) {
        return null;
    }

    @Override
    public OperaResponse getAddressInfo(AddressInfoQueryBean bean) {
        return null;
    }

    @Override
    public OperaResponse preHoldSkuInventory(HoldSkuInventoryQueryBean bean) {
        return null;
    }

    @Override
    public OperaResponse releaseSkuInventory(ReleaseSkuInventoryQueryBean bean) {
        return null;
    }
}
