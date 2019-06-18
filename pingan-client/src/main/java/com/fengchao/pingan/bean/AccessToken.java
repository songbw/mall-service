package com.fengchao.pingan.bean;

import lombok.Data;

@Data
public class AccessToken {
    private String expiresIn;
    private String accessToken;
}
