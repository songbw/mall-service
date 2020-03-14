package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author song
 * @2020-02-26 4:13 下午
 **/
@Setter
@Getter
public class WKRefundRequestBean {
    private String merchantNo  ;
    private String orderNo ;
    private String refundNo;
    private String paymentNo;
    private String notifyUrl;
    private BigDecimal refundAmount;
}
