package com.fengchao.sso.bean;

import lombok.Data;

@Data
public class AccessToken {
    private String expiresIn;
    private String accessToken;
    private String openId;
    private String payId;
}
