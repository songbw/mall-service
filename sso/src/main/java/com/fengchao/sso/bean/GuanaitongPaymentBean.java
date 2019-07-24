package com.fengchao.sso.bean;

import lombok.Data;

@Data
public class GuanaitongPaymentBean {
    private String outer_trade_no;
    private String buyer_open_id ;
    private String reason;
    private Float total_amount ;
    private String trade_info ;
    private String return_url ;
    private String notify_url ;
}
