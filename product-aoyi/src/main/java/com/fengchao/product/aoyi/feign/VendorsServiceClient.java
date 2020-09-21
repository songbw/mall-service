package com.fengchao.product.aoyi.feign;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.feign.hystric.VendorsServiceClientFallbackFactory;
import com.fengchao.product.aoyi.rpc.extmodel.SysCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "vendors", fallbackFactory = VendorsServiceClientFallbackFactory.class)
public interface VendorsServiceClient {

    @RequestMapping(value = "/vendors/companyById", method = RequestMethod.GET)
    OperaResponse<SysCompany> vendorInfo(@RequestParam("id") int id);

    @RequestMapping(value = "/vendors/company/list", method = RequestMethod.GET)
    OperaResponse<List<SysCompany>> queryAllMerchantList();

    @RequestMapping(value = "/renter/api/companies", method = RequestMethod.GET)
    OperaResponse<List<Integer>> queryRenterMerchantList(@RequestParam("renterId") String renterId);

    @RequestMapping(value = "/renter/api/renterId", method = RequestMethod.GET)
    OperaResponse<String> queryRenterId(@RequestParam("appId") String appId );

    @RequestMapping(value = "/renter/api/appIdList", method = RequestMethod.GET)
    OperaResponse<List<String>> queryAppIdList(@RequestParam("renterId") String renterId);

    @RequestMapping(value = "/renter/api/companiesByAppId/{appId}", method = RequestMethod.GET)
    OperaResponse<List<Integer>> queryAppIdMerchantList(@PathVariable("appId") String appId);

}
