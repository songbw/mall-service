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
}
