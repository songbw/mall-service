package com.fengchao.equity.bean;

import lombok.Data;

@Data
public class CouponResquest {
    private String sign;
    private String timestamp;
    private Object data = new Object();
}
