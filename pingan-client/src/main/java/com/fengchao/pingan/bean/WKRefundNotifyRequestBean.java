package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author song
 * @2020-02-26 6:14 下午
 **/
@Setter
@Getter
public class WKRefundNotifyRequestBean {
    private String refundNo ;
    private String orderNo ;
    private BigDecimal refundAmount ;
    private String refundTime ;
    private String status ;
}
