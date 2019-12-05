package com.fengchao.order.bean.vo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Getter
@Setter
public class DailyExportOrderStatisticVo {

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 已完成子订单数量
     */
    private Long completedOrderCount;

    /**
     * 已发货子订单数量
     */
    private Long deliveredOrderCount;

    /**
     * 未发货子订单数量
     */
    private Long unDeliveryOrderCount;

    /**
     * 未发货子订单中，最早时间的自订单号
     */
    private String unDeliveryEarliestOrderNo;

    /**
     * 未发货子订单中, 最早时间的子订单交易时间
     */
    private String unDeliveryEarliestOrderTime;
}
