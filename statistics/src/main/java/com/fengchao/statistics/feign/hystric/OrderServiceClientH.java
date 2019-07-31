package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResponse;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.OrderServiceClient;
import com.fengchao.statistics.rpc.extmodel.DayStatisticsBean;
import com.fengchao.statistics.rpc.extmodel.OrderDetailBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderServiceClientH implements OrderServiceClient {

    @Override
    public OperaResponse<DayStatisticsBean> statistics() {
        return HystrixDefaultFallback.fallbackResponse();
    }

    @Override
    public OperaResult paymentCount(String start, String end) {
        return HystrixDefaultFallback.defaultFallback();
    }

    @Override
    public OperaResult paymentPromotionCount(String start, String end) {
        return HystrixDefaultFallback.defaultFallback();
    }

//    @Override
//    public OperaResult paymentMerchantCount(String start, String end) {
//        return HystrixDefaultFallback.defaultFallback();
//    }

    @Override
    public OperaResponse<List<OrderDetailBean>> queryPayedOrderDetailList(String start, String end) {
        return HystrixDefaultFallback.fallbackResponse();
    }
}
