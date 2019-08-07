package com.fengchao.product.aoyi.feign;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.PageBean;
import com.fengchao.product.aoyi.bean.ProductQueryBean;
import com.fengchao.product.aoyi.feign.hystric.ESServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "elasticsearch-mall", fallback = ESServiceH.class)
public interface ESService {

    @RequestMapping(value = "/es/prod/search", method = RequestMethod.POST)
    OperaResponse<PageBean> search(@RequestBody ProductQueryBean queryBean);

}
