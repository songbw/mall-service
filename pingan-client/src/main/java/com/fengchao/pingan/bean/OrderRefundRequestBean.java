package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/11/29 16:01
 */
@Setter
@Getter
public class OrderRefundRequestBean {
    private String merchantNo;
    private String oriOrderNo;
    private Long refundAmt ;
    private String refundMchOrderNo  ;
//    private String notifyUrl ;
}
