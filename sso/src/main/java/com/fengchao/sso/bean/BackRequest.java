package com.fengchao.sso.bean;

import lombok.Data;

@Data
public class BackRequest {
    private BackBean data;
    private String sign;
}
