package com.fengchao.statistics.feign.hystric;

import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.feign.OrderServiceClient;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceClientH implements OrderServiceClient {

    @Override
    public OperaResult statistics(String start, String end) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取订单服务失败 " + start + " " + end);
        return result;
    }

    @Override
    public OperaResult paymentCount(String start, String end) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取订单服务失败 " + start + " " + end);
        return result;
    }

    @Override
    public OperaResult paymentPromotionCount(String start, String end) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取订单服务失败 " + start + " " + end);
        return result;
    }

    @Override
    public OperaResult paymentMerchantCount(String start, String end) {
        OperaResult result = new OperaResult();
        result.setCode(404);
        result.setMsg("获取订单服务失败 " + start + " " + end);
        return result;
    }

    @Override
    public OperaResult paymentCategoryList(String start, String end) {
        return null;
    }
}
