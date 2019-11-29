package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/11/29 16:01
 */
@Setter
@Getter
public class BackNotifyRequestBean {
    private String appId ;
    private Long amount;
    private String orderNo  ;
    private String mchOrderNo ;
    private String timeEnd  ;
    private String sign  ;
}
