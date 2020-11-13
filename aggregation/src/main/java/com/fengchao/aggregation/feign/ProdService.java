package com.fengchao.aggregation.feign;

import com.fengchao.aggregation.bean.OperaResult;
import com.fengchao.aggregation.feign.hystric.ProdServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "product-aoyi", fallback = ProdServiceH.class)
public interface ProdService {

    @RequestMapping(value = "/prod/getByMpus", method = RequestMethod.GET)
    OperaResult findProductListByMpuIdList(@RequestParam("mpuIdList") List<String> mpuIdList, @RequestParam("appId") String appId);

    @RequestMapping(value = "/adminProd/getByMpus", method = RequestMethod.GET)
    OperaResult findProductListByMpuIdListAdmin(@RequestParam("mpuIdList") List<String> mpuIdList, @RequestParam("appId") String appId);
}
