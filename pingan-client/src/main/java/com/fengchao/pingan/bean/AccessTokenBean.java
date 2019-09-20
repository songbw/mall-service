package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenBean {
    private int expiresIn;
    private String accessToken ;
    private String refreshToken ;
}
