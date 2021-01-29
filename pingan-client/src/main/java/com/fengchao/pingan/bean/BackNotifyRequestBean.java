package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author songbw
 * @date 2019/11/29 16:01
 */
@Setter
@Getter
public class BackNotifyRequestBean implements Serializable {
    private String appId ;
    private Long amount;
    private String orderNo  ;
    private String mchOrderNo ;
    private String timeEnd  ;
    private String sign  ;
    private String payType = "WECHAT" ;
    private String msgType = "PAY" ;
    private String chl_order_no ;
}
