package com.fengchao.sso.feign;

import com.fengchao.sso.feign.hystric.VendorsServiceClientFallbackFactory;
import com.fengchao.sso.rpc.extmodel.SysCompanyX;
import com.fengchao.sso.bean.OperaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:26
 */
@FeignClient(value = "vendors", fallbackFactory = VendorsServiceClientFallbackFactory.class)
public interface VendorsServiceClient {

    @RequestMapping(value = "/vendors/company/list", method = RequestMethod.GET)
    OperaResponse<List<SysCompanyX>> queryAllCompanyList();

    @RequestMapping(value = "/renter/api/companies", method = RequestMethod.GET)
    OperaResponse<List<Integer>> queryRenterMerchantList(@RequestParam("renterId") String renterId);

    @RequestMapping(value = "/renter/api/renterId", method = RequestMethod.GET)
    OperaResponse<String> queryRenterId(@RequestParam("appId") String appId );

    @RequestMapping(value = "/renter/api/appIdList", method = RequestMethod.GET)
    OperaResponse<List<String>> queryAppIdList(@RequestHeader("renterId") String renterId);

    @RequestMapping(value = "/renter/api/companiesByAppId/{appId}", method = RequestMethod.GET)
    OperaResponse<List<Integer>> queryAppIdMerchantList(@PathVariable("appId") String appId);
}
