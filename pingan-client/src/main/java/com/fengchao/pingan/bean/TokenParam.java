package com.fengchao.pingan.bean;

import lombok.Data;

@Data
public class TokenParam {
    private String appId = "fengcao";
    private String timestamp = "";
    private String randomSeries = "";
    private String initCode = "";
}
