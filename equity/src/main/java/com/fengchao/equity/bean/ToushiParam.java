package com.fengchao.equity.bean;

import lombok.Data;

@Data
public class ToushiParam{

    private String open_id;
    private String coupon_code;
    private String merchantId;
    private String merchantName;
    private TouShiCoupon coupon;
}