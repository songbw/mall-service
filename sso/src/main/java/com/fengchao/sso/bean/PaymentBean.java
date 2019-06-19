package com.fengchao.sso.bean;

import lombok.Data;

@Data
public class PaymentBean {
    private String openId;
    private String appId;
    private String merchantNo;
    private String orderNos;
    private String goodsName;
    private int amount;
}
