package com.fengchao.pingan.bean;

import lombok.Data;

@Data
public class RefundParam {
    // 订单总金额
    private int totalFee = 0;
    // 退款金额
    private int refundFee = 0;
    // 退款结果回调地址
    private String noticeUrl = "";
    // 商家发起的退款订单号，新的一笔订单号
    private String outRefundNo;
    // 商家的原订单号。下单时商户传入的订单号
    private String sourceOutTradeNo;
    // 与sourceOutTradeNo相对应，统一支付平台的原订单号
    private String sourceOrderNo;
    // 商户编号
    private String merchantCode = "10000007";
}
