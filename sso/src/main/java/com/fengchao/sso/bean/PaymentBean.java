package com.fengchao.sso.bean;

import lombok.Data;

@Data
public class PaymentBean {
    private String openId;
    private String iAppId;   // 凤巢APPID
    private String tAppId;   // 第三方APPID
    private String merchantNo;
    private String orderNos;
    private String goodsName;
    private int amount;
}
