package com.fengchao.base.feign.hystric;

import com.fengchao.base.bean.OperaResponse;
import com.fengchao.base.feign.ProductService;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceH implements ProductService {

    @Setter
    private Throwable cause;

    @Override
    public OperaResponse imageBack(Long id, Integer status) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }
}
