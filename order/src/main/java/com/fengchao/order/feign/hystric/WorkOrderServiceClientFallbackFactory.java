package com.fengchao.order.feign.hystric;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.feign.VendorsServiceClient;
import com.fengchao.order.feign.WorkOrderServiceClient;
import com.fengchao.order.rpc.extmodel.SysCompanyX;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-27 上午10:32
 */
@Component
@Slf4j
public class WorkOrderServiceClientFallbackFactory implements FallbackFactory<WorkOrderServiceClient> {

    @Override
    public WorkOrderServiceClient create(Throwable throwable) {
        return new WorkOrderServiceClient() {

            @Override
            public OperaResponse<List<String>> queryRefundedOrderDetailIdList(Long merchantId,
                                                                              String startTime,
                                                                              String endTime) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

        };
    }

}
