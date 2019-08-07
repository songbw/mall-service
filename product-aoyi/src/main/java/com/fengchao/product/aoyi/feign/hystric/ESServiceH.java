package com.fengchao.product.aoyi.feign.hystric;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.feign.ESService;
import org.springframework.stereotype.Component;

@Component
public class ESServiceH implements ESService {
    @Override
    public OperaResponse<PageBean> search(ProductQueryBean queryBean) {
        return HystrixDefaultFallback.defaultReponseFallback();
    }
}
