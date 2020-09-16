package com.fengchao.product.aoyi.feign.hystric;

import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.feign.VendorsServiceClient;
import com.fengchao.product.aoyi.rpc.extmodel.SysCompany;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:32
 */
@Component
@Slf4j
public class VendorsServiceClientFallbackFactory implements FallbackFactory<VendorsServiceClient> {

    @Override
    public VendorsServiceClient create(Throwable throwable) {
        return new VendorsServiceClient() {
            @Override
            public OperaResponse<List<SysCompany>> queryAllMerchantList() {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

            @Override
            public OperaResponse queryRenterMerchantList(String renterId) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

            @Override
            public OperaResponse vendorInfo(int id) {
                OperaResponse result = new OperaResponse();
                result.setCode(404);
                result.setMsg("多商户服务失败 " + id);
                return result;
            }
        };
    }

}
