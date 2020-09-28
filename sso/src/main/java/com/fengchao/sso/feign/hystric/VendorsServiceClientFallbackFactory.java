package com.fengchao.sso.feign.hystric;

import com.fengchao.sso.bean.OperaResponse;
import com.fengchao.sso.feign.VendorsServiceClient;
import com.fengchao.sso.rpc.extmodel.SysCompanyX;
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
            public OperaResponse<List<SysCompanyX>> queryAllCompanyList() {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

            @Override
            public OperaResponse<List<Integer>> queryRenterMerchantList(String renterId) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

            @Override
            public OperaResponse<String> queryRenterId(String appId) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

            @Override
            public OperaResponse<List<String>> queryAppIdList(String renterId) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

            @Override
            public OperaResponse<List<Integer>> queryAppIdMerchantList(String appId) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

        };
    }

}
