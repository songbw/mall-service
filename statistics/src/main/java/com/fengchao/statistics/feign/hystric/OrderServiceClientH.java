package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.OrderServiceClient;
import com.fengchao.statistics.rpc.extmodel.DayStatisticsBean;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderServiceClientH implements OrderServiceClient {

    @Setter
    private Throwable cause;

    @Override
    public OperaResponse<DayStatisticsBean> statistics() {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }

    @Override
    public OperaResult paymentCount(String start, String end) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResult paymentPromotionCount(String start, String end) {
        return HystrixDefaultFallback.defaultFallback(cause);
    }

    @Override
    public OperaResponse<List<OrderDetailBean>> queryPayedOrderDetailList(String start, String end) {
        return HystrixDefaultFallback.fallbackResponse(cause);
    }
}
