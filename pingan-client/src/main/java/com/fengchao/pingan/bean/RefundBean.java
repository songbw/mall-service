package com.fengchao.pingan.bean;

import lombok.Data;

@Data
public class RefundBean {
    private String openId;
    private String appId;
    // 商家发起的退款订单号，新的一笔订单号
    private String outRefundNo;
    // 商家的原订单号。下单时商户传入的订单号
    private String sourceOutTradeNo;
    // 与sourceOutTradeNo相对应，统一支付平台的原订单号
    private String sourceOrderNo;
    private int amount;
}
