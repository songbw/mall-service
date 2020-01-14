package com.fengchao.order.bean.vo;

import com.fengchao.order.constants.AppPlatformEnum;
import com.fengchao.order.constants.PaymentTypeEnum;
import com.fengchao.order.constants.SettlementTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Getter
@Setter
public class ExportShareProfitVo {

    /**
     * 平台表识别
     */
    private AppPlatformEnum appPlatformEnum;

    /**
     * 支付方式
     */
    private PaymentTypeEnum paymentTypeEnum;

    /**
     * 结算类型
     */
    private SettlementTypeEnum settlementTypeEnum;

    /**
     * （支付方式、结算类型）维度下的总金额 单位分
     */
    private Integer amout;
}
