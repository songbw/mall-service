package com.fengchao.sso.bean;

import lombok.Data;

@Data
public class GATBackBean {
    private String appid;
    private String outer_trade_no ;
    private String buyer_open_id ;
    private String attach ;
    private float total_amount;
    private String trade_no ;
    private String timestamp ;
    private String sign ;
}
