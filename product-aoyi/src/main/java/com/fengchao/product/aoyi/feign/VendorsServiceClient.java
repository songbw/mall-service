package com.fengchao.product.aoyi.feign;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.bean.OperaResult;
import com.fengchao.product.aoyi.feign.hystric.VendorsServiceClientFallbackFactory;
import com.fengchao.product.aoyi.rpc.extmodel.SysCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "vendors", fallbackFactory = VendorsServiceClientFallbackFactory.class)
public interface VendorsServiceClient {

    @RequestMapping(value = "/vendors/{id}", method = RequestMethod.GET)
    OperaResult vendorInfo(@PathVariable("id") int id);

    @RequestMapping(value = "/vendors/company/list", method = RequestMethod.GET)
    OperaResponse<List<SysCompany>> queryAllMerchantList();

}
