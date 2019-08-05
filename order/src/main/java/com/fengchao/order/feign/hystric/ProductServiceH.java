package com.fengchao.order.feign.hystric;

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
}
