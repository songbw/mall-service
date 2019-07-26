package com.fengchao.statistics.feign;

import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.hystric.ProductServiceClientH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "product-aoyi", fallback = ProductServiceClientH.class)
public interface ProductServiceClient {

    @RequestMapping(value = "/merchantCode", method = RequestMethod.GET)
    OperaResult findMerchant(@RequestParam("id") int id);

    /**
     * 根据categoryId集合查询品类列表
     *
     * @param categoryIdList
     * @return
     */
    @RequestMapping(value = "/adminCategory/category/listByIds", method = RequestMethod.GET)
    OperaResponse queryCategorysByCategoryIdList(@RequestParam("categoryIdList") List<Integer> categoryIdList);

}
