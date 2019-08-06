package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.feign.VendorsServiceClient;
import com.fengchao.statistics.rpc.extmodel.ResultObject;
import com.fengchao.statistics.rpc.extmodel.SysCompany;
import com.fengchao.statistics.rpc.extmodel.SysUser;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:32
 */
@Component
@Slf4j
public class VendorsServiceClientH implements VendorsServiceClient {

    @Setter
    private Throwable cause;

    @Override
    public ResultObject<List<SysCompany>> queryMerchantByIdList(List<Long> idList) {
        log.info("根据id集合查询商户信息 调用vendors rpc服务 降级:{}", cause.getMessage(), cause);

        ResultObject resultObject = new ResultObject<>(500, "vendors 服务降级", null);
        return resultObject;
    }
}
