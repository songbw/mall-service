package com.fengchao.equity.feign;

import com.alibaba.fastjson.JSONObject;
import com.fengchao.equity.bean.OperaResponse;
import com.fengchao.equity.bean.OperaResult;
import com.fengchao.equity.feign.hystric.VendorsServiceH;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "vendors", fallback = VendorsServiceH.class)
public interface VendorsService {

    @RequestMapping(value = "/vendors/{id}", method = RequestMethod.GET)
    OperaResult vendorInfo(@PathVariable("id") int id);

    @RequestMapping(value = "/welfare/employees/byPhone", method = RequestMethod.GET)
    OperaResult getEmployeeInfoByPhone(@RequestParam("phone")String phone);

    @RequestMapping(value = "/welfare/employees/byPhoneList", method = RequestMethod.GET)
    OperaResult getEmployeeInfoByPhoneList(@RequestParam("phoneList") List<String> phoneList);

}
