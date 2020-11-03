package com.fengchao.equity.feign;

import com.fengchao.equity.bean.OperaResponse;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.feign.hystric.VendorsServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "vendors", fallback = VendorsServiceH.class)
public interface VendorsService {

    @RequestMapping(value = "/vendors/{id}", method = RequestMethod.GET)
    OperaResult vendorInfo(@PathVariable("id") int id);

    @RequestMapping(value = "/renter/api/renterId", method = RequestMethod.GET)
    OperaResponse<String> queryRenterId(@RequestParam("appId") String appId );

}
