package com.fengchao.sso.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/11/29 16:01
 */
@Setter
@Getter
public class PingAnPaymentBean {
    private String merchantNo;
    private String goodsName;
    private int amount;
    private String memberNo ;
    private String mchOrderNo ;
    private String notifyUrl ;
}
