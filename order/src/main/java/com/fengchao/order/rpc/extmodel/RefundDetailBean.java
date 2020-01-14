package com.fengchao.order.rpc.extmodel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefundDetailBean {

    private String createDate; // ": "2019-12-20T16:11:47",
    private String merchantCode; // ": "32",
    private String orderNo; // ": "e2f2cf41ca674b51aeaeaac8a584fa79",
    private String outRefundNo; // ": "111576829506646",
    private String payType; // ": "balance",
    private String refundFee; // ": "10", // 单位分
    private String refundNo; // ": "",
    private String sourceOutTradeNo; // ": "118111a3bf2248b9e6404c87dff0892572ebb953283501",
    private Integer status; // ": 1,
    private String statusMsg; // ": "退款成功",
    private String totalFee; // ": "0",
    private String tradeDate; // ": "20191220161146"
}
