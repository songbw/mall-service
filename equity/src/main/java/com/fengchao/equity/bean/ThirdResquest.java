package com.fengchao.equity.bean;

import lombok.Data;

@Data
public class ThirdResquest {
    private String timestamp;
    private String sign = "";
    private Object data = new Object();
}
