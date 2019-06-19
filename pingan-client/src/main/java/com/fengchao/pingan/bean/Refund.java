package com.fengchao.pingan.bean;

import lombok.Data;

@Data
public class Refund {
    // 1申请成功等待退款
    private int resultCode ;
    // 统一支付平台的订单号，新的订单号
    private String orderNo;
    // 商户编号,resultCode成功才有值
    private String merchantCode;
    // 商家系统内部的退款单号,业务系统内部唯一,resultCode成功才有值
    private String outRefundNo ;
    // 原订单总金额,resultCode成功才有值
    private int totalFee;
    // 退款金额,resultCode成功才有值.
    private int refundFee;
    // 商家的原订单号
    private String sourceOutTradeNo ;
    // 统一支付平台的原订单号，与sourceOutTradeNo相对应
    private String sourceOrderNo ;
}
