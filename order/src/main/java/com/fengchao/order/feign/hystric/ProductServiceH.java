package com.fengchao.order.feign.hystric;

import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.feign.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceH implements ProductService {
    @Override
    public OperaResult find(String id) {
        return HystrixDefaultFallback.defaultFallback();
    }

    @Override
    public OperaResult findProductListByMpuIdList(List<String> mpuIdList) {
        return HystrixDefaultFallback.defaultFallback();
    }
}
