package com.fengchao.order.feign.hystric;

import com.fengchao.order.bean.InventoryMpus;
import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.ProductService;
import com.fengchao.order.model.AoyiProdIndex;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceH implements ProductService {

    @Setter
    private Throwable cause;

    @Override
    public OperaResult find(String id, String appId) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResult findProductListByMpuIdList(List<String> mpuIdList) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResult selectProductListByMpuIdList(List<String> mpuIdList) {
        return null;
    }

    @Override
    public OperaResult inventorySub(List<InventoryMpus> inventories) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResult inventoryAdd(List<InventoryMpus> inventories) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResult selectPlatformByAppId(String appId) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResponse selectByMpuIdListAndSkuCodes(List<AoyiProdIndex> mpuIdList) {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }
}
