package com.fengchao.pingan.bean;

import lombok.Data;

@Data
public class TokenResult {
    private String code;
    private AccessToken data;
    private String message;
    private boolean success;
}
