package com.fengchao.sso.bean;

import lombok.Data;

@Data
public class BackBean {
    // 商户号，充值钱包的时候没有
    private String payee ;
    // 退款金额，退款时候有
    private int refundFee;
    // 支付方式
    private String payType;
    //支付系统订单号
    private String orderNo;
    //实际退款金额
    private int actPayFree;
    // 订单总金额
    private int totalFee;
    // 商户订单号
    private String outTradeNo;
    // C端个人账号。 表示唯一用户
    private String payer;
    // 支付状态 10初始创建订单  1下单成功，等待支付。  2支付中，3超时未支付  4支付失败  5支付成功  11支付成功，记账也成功   12支付成功，记账失败  14退款失败，15订单已退款
    private int payStatus;
    // 1支付，2充值，3退款，4提现
    private int orderCategory ;
}
