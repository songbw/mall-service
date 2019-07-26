package com.fengchao.equity.bean;

import lombok.Data;

@Data
public class ThirdResult {
    private String sign;
    private String timestamp;
    private ThirdCouponParam data;
}
