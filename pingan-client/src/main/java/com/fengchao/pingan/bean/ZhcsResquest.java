package com.fengchao.pingan.bean;

import lombok.Data;

@Data
public class ZhcsResquest {
    private String appId = "fengcao";
    private String sign = "";
    private Object message = new Object();
}
