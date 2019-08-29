package com.fengchao.product.aoyi.feign;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.feign.hystric.VendorsServiceClientFallbackFactory;
import com.fengchao.product.aoyi.rpc.extmodel.SysCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:26
 */
@FeignClient(value = "vendors", fallbackFactory = VendorsServiceClientFallbackFactory.class)
public interface VendorsServiceClient {

    @RequestMapping(value = "/vendors/company/list", method = RequestMethod.GET)
    OperaResponse<List<SysCompany>> queryAllMerchantList();
}
