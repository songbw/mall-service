package com.fengchao.pingan.bean;

import lombok.Data;

@Data
public class PaymentResult {
    private String returnCode;
    private OrderNo data;
    private String sign;
    private String msg;
}
