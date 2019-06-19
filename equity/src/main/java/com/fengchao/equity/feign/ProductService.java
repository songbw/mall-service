package com.fengchao.equity.feign;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.QueryProdBean;
import com.fengchao.equity.feign.hystric.ProductServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "product-aoyi", fallback = ProductServiceH.class)
public interface ProductService {
    @RequestMapping(value = "/adminCategory/categoryList", method = RequestMethod.POST)
    OperaResult findCategoryList(@RequestBody List<String> categories);

    @RequestMapping(value = "/adminProd/prodAll", method = RequestMethod.POST)
    OperaResult findProdList(@RequestBody QueryProdBean queryProdBean);
}
