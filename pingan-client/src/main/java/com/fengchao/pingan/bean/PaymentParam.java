package com.fengchao.pingan.bean;

import lombok.Data;

@Data
public class PaymentParam {
    private String notifyUrl;
    /**
     * 支付方式：
     * 1. 支付宝支付（直连）：ALIPAY-ZL
     * 2.支付宝支付（见证宝）:ALIPAY-JZB
     * 2. 微信支付-WECHAT
     * 3. 钱包账户支付-AT01
     * 4. 行业账户支付-AT02开头，具体由业务配置
     */
    private String payType = "ALIPAY-ZL";
    private String payer = "600063674413" ;
    private String payee = "10000007" ;
    private String outTradeNo = "";
    private String body = "";
    private String remark = "";
    private int totalFee = 0;
    // 实际支付金额
    private int actPayFee = 0;
    private String limitPay = "";
    // 1支付，2充值，3退款
    private int orderCategory = 1;
}
