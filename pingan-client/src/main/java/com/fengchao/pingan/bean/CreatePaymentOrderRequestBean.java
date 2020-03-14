package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/11/29 16:01
 */
@Setter
@Getter
public class CreatePaymentOrderRequestBean {
    private String appId ;
    private String merchantNo;
    private String goodsName;
    private Long amount;
    private String memberNo ;
    private String mchOrderNo ;
    private String notifyUrl ;
    private String sceneId ;
}
