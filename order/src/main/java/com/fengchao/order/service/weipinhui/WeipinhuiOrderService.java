package com.fengchao.order.service.weipinhui;

import com.fengchao.order.bean.*;
import com.fengchao.order.rpc.extmodel.weipinhui.AoyiQueryInventoryResDto;

import java.util.List;

/**
 * 唯品会order服务
 */
public interface WeipinhuiOrderService {

    /**
     * 唯品会查询库存
     *
     * @param itemId
     * @param skuId
     * @param num
     * @param divisionCode
     * @return
     */
    OperaResponse<String> queryItemInventory(String itemId, String skuId,
                                                                      Integer num, String divisionCode);

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
