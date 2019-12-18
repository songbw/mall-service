package com.fengchao.order.feign.hystric;

import com.fengchao.order.bean.OperaResponse;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.bean.vo.BillExportReqVo;
import com.fengchao.order.feign.WsPayServiceClient;
import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;
import com.fengchao.order.rpc.extmodel.PayInfoBean;
import com.fengchao.order.rpc.extmodel.RefundMethodInfoBean;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author tom
 * @Date 19-7-27 上午10:32
 */
@Component
@Slf4j
public class WspayServiceClientFallbackFactory implements FallbackFactory<WsPayServiceClient> {

    @Override
    public WsPayServiceClient create(Throwable throwable) {

        return new WsPayServiceClient() {

            @Override
            public OperaResponse<Map<String, List<OrderPayMethodInfoBean>>> queryBatchPayMethod(List<String> orderNoList) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

            @Override
            public OperaResponse<PayInfoBean> queryConsumeRefundUsing(BillExportReqVo billExportReqVo) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }

            @Override
            public OperaResponse<Map<String, List<RefundMethodInfoBean>>> queryBatchRefundMethod(List<String> orderNoList) {
                return HystrixDefaultFallback.fallbackResponse(throwable);
            }
        };
    }

}
