package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCodeBean {
    private int expiresIn;
    private String userAccessToken ;
    private boolean verifyResult ;
}
