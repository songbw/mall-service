package com.fengchao.statistics.feign;

import com.fengchao.statistics.feign.hystric.VendorsServiceClientH;
import com.fengchao.statistics.rpc.extmodel.ResultObject;
import com.fengchao.statistics.rpc.extmodel.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:26
 */
@FeignClient(value = "vendors", fallback = VendorsServiceClientH.class)
public interface VendorsServiceClient {

    @RequestMapping(value = "/vendors/listByIds", method = RequestMethod.GET)
    ResultObject<List<SysUser>> queryMerchantByIdList(@RequestParam("idList") List<Long> idList);
}
