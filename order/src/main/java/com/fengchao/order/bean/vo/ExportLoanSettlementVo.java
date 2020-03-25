package com.fengchao.order.bean.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 导出
 */
@Setter
@Getter
public class ExportLoanSettlementVo {

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 结算周期
     */
    private String settlementPeriod;

    /**
     * 已完成订单金额 单位元
     */
    private String completeOrderAmount;

    /**
     * 已退款订单金额 单位元
     */
    private String refundOrderAmount;

    /**
     * 实际成交订单金额 单位元
     */
    private String realyOrderAmount;

    /**
     * 优惠券金额 单位元
     */
    private String couponAmount;

    /**
     * 运费金额 单位元
     */
    private String expressFee;

    /**
     * 应付款 单位元
     */
    private String payAmout;
}
