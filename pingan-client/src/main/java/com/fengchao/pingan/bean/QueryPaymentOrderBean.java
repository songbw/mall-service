package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/11/29 17:03
 */
@Setter
@Getter
public class QueryPaymentOrderBean {
    private String orderNo  ;
    private String mchOrderNo ;
    private String statue ;
    private String tradeTime ;
}
