package com.fengchao.equity.feign;

import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.bean.QueryProdBean;
import com.fengchao.equity.bean.vo.PageVo;
import com.fengchao.equity.feign.hystric.ProdServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "product-aoyi", fallback = ProdServiceH.class)
public interface ProdService {
    @RequestMapping(value = "/adminCategory/categoryList", method = RequestMethod.POST)
    OperaResult findCategoryList(@RequestBody List<String> categories);

    @RequestMapping(value = "/adminProd/prodAll", method = RequestMethod.POST)
    OperaResult findProdList(@RequestBody QueryProdBean queryProdBean, @RequestHeader("appId") String appId);

    @RequestMapping(value = "/prod/mpuIds", method = RequestMethod.GET)
    OperaResult findProductListByMpuIdList(@RequestParam("mpuIdList") List<String> mpuIdList);

    @RequestMapping(value = "/adminProd/search", method = RequestMethod.POST)
    OperaResult searchProd(@RequestBody QueryProdBean queryProdBean, @RequestHeader("merchant") Integer merchantHeader);

    /**
     * 获取平台信息
     * @return
     */
    @RequestMapping(value = "/platform/all", method = RequestMethod.POST)
    OperaResult selectPlatformAll(@RequestBody PageVo pageVo);
}
