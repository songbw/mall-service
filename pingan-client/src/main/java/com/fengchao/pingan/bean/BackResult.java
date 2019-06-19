package com.fengchao.pingan.bean;

import lombok.Data;

@Data
public class BackResult {
    private String returnCode;
    private Refund data;
    private String sign;
    private boolean msg;
}
