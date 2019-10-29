package com.fengchao.order.feign.hystric;

import com.fengchao.order.bean.InventoryMpus;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.ProductService;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceH implements ProductService {

    @Setter
    private Throwable cause;

    @Override
    public OperaResult find(String id) {
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
}
