package com.fengchao.order.feign;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.rpc.extmodel.SysCompanyX;
import com.fengchao.statistics.feign.hystric.VendorsServiceClientFallbackFactory;
import com.fengchao.statistics.rpc.extmodel.ResultObject;
import com.fengchao.statistics.rpc.extmodel.SysCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:26
 */
@FeignClient(value = "vendors", fallbackFactory = VendorsServiceClientFallbackFactory.class)
public interface VendorsServiceClient {

    @RequestMapping(value = "/vendors/company/list", method = RequestMethod.GET)
    OperaResponse<List<SysCompanyX>> queryAllCompanyList();
}
