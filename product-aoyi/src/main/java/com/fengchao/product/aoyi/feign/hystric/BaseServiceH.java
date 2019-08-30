package com.fengchao.product.aoyi.feign.hystric;

import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.feign.BaseService;
import com.fengchao.product.aoyi.model.AyFcImages;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaseServiceH implements BaseService {

    @Override
    public OperaResult downUpload(List<AyFcImages> images) {
        return HystrixDefaultFallback.defaultFallback(new Throwable());
    }
}
