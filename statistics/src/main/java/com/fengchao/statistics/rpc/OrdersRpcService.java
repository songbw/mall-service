package com.fengchao.statistics.rpc;

import com.alibaba.fastjson.JSON;
import com.fengchao.statistics.bean.MerchantPaymentBean;
import com.fengchao.statistics.bean.OperaResult;
import com.fengchao.statistics.bean.QueryBean;
import com.fengchao.statistics.feign.OrderServiceClient;
import com.fengchao.statistics.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author tom
 * @Date 19-7-25 下午2:29
 */
@Service
@Slf4j
public class OrdersRpcService {

    private OrderServiceClient orderServiceClient;

    @Autowired
    public OrdersRpcService(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    /**
     * 按商户统计订单支付总额
     *
     * @param queryBean
     * @return
     */
    public List<MerchantPaymentBean> statisticOrdersAmountByMerchant1(QueryBean queryBean) {
        // 返回值
        List<MerchantPaymentBean> merchantPaymentBeanList = new ArrayList<>();

        // 执行rpc调用
        log.info("按商户统计订单支付总额 调用order rpc服务 入参:{}", JSONUtil.toJsonString(merchantPaymentBeanList));
        OperaResult operaResult = orderServiceClient.paymentMerchantCount(queryBean.getStartTime(), queryBean.getEndTime());
        log.info("按商户统计订单支付总额 调用order rpc服务 返回:{}", JSONUtil.toJsonString(operaResult));

        // 处理返回
        if (operaResult.getCode() == 200) {
            List<MerchantPaymentBean> _merchantPaymentBeanList
                    = (List<MerchantPaymentBean>) operaResult.getData().get("result");

            // 转 PromotionBean
            merchantPaymentBeanList = JSON.parseArray(JSON.toJSONString(_merchantPaymentBeanList), MerchantPaymentBean.class);
        } else {
            log.warn("按商户统计订单支付总额 调用order rpc服务 错误!");
        }

        log.info("OrdersRpcService#statisticOrdersAmountByMerchant1 调用equity rpc服务 返回:{}",
                JSONUtil.toJsonString(merchantPaymentBeanList));

        return merchantPaymentBeanList;
    }
}