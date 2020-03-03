package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author song
 * @2020-02-26 4:16 下午
 **/
@Setter
@Getter
public class WKRefund {
    private String paymentOrderNo ;
    private String refundNo  ;
    private String serialNo ;
    private Integer status ;
    private String amount ;
}
