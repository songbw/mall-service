package com.fengchao.order.service.weipinhui;

import com.fengchao.order.bean.*;

import java.util.List;

/**
 * 唯品会order服务
 */
public interface WeipinhuiOrderService {

    /**
     * 预占订单
     */
    OperaResponse<String> renderOrder(OrderParamBean orderParamBean, OrderMerchantBean orderMerchantBean,
                                      OrderCouponBean coupon, List<InventoryMpus> inventories);

    /**
     * 确认订单
     *
     * 注意
     * 1. 首先从orderIds(主订单的id集合)筛选出唯品会的订单
     * 2. 调用唯品会rpc的确认订单接口
     *
     * @param orderIds 主订单的id集合
     * @return
     */
    OperaResponse<String> createOrder(List<Integer> orderIds);
}
