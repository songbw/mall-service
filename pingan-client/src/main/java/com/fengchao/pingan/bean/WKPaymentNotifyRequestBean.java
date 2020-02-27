package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author songbw
 * @date 2019/11/29 16:01
 */
@Setter
@Getter
public class WKPaymentNotifyRequestBean implements Serializable {
    // 业务订单号
    private String orderNo ;
    // 订单金额
    private BigDecimal orderAmount;
    // 订单标题
    private String orderSubject  ;
    // 支付订单号
    private String paymentNo ;
    // 支付金额
    private BigDecimal paymentAmount  ;
    // 支付时间
    private String paymentTime  ;
    // 支付方式
    private String payMethod ;
    // 结算日期
    private String settleDate ;
    // 结算金额
    private BigDecimal settleAmount ;
    // 订单状态
    private String status ;
}
