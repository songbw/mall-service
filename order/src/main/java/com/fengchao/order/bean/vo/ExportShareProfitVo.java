package com.fengchao.order.bean.vo;

import com.fengchao.order.constants.PaymentTypeEnum;
import com.fengchao.order.constants.SettlementTypeEnum;

/**
 *
 */
public class ExportShareProfitVo {

    private String appId;

    private String appName;

    /**
     * 支付方式
     */
    private PaymentTypeEnum paymentTypeEnum;

    /**
     * 结算类型
     */
    private SettlementTypeEnum settlementTypeEnum;

    /**
     * （支付方式、结算类型）维度下的总金额
     */
    private String amout;
}
