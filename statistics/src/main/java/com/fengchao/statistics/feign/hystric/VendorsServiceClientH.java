package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.feign.VendorsServiceClient;
import com.fengchao.statistics.rpc.extmodel.ResultObject;
import com.fengchao.statistics.rpc.extmodel.SysCompany;
import com.fengchao.statistics.rpc.extmodel.SysUser;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:32
 */
@Component
public class VendorsServiceClientH implements VendorsServiceClient {

    @Override
    public ResultObject<List<SysCompany>> queryMerchantByIdList(List<Long> idList) {
        ResultObject resultObject = new ResultObject<>(500, "vendors 服务降级", null);
        return resultObject;
    }
}
