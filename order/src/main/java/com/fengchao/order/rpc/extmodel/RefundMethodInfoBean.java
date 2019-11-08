package com.fengchao.order.rpc.extmodel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class RefundMethodInfoBean {

    // 创建时间
    private LocalDateTime createDate;

    //商户编号
    private String merchantCode;

   //支付订单号
    private String orderNo;

   //退款号
    private String outRefundNo;

   //支付方式
    private String payType;

   //交易实际金额
    private String refundFee;

   //退款号
    private String refundNo;

   //原订单号
    private String sourceOutTradeNo;

    //交易状态: 1: 成功, 2: 失败, 0: 新创建
    private Integer status;

    //交易状态解释
    private String statusMsg;

    //交易总金额
    private String totalFee;

   //退款时间
    private String tradeDate;

}
