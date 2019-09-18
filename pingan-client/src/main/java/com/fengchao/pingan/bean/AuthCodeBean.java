package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthCodeBean {
    private int expiresIn;
    private String authCode ;
}
