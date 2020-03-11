package com.fengchao.sso.bean;

import lombok.Data;

@Data
public class LoginBean {
    private String username;
    private String password;
    private String oldPassword;
    private String code;
    private String appId;
    private String openId ;
}
