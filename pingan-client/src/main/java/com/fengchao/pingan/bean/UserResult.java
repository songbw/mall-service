package com.fengchao.pingan.bean;

import lombok.Data;

@Data
public class UserResult {
    private String code;
    private UserInfo data;
    private String message;
    private boolean success;
}
