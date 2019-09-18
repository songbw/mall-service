package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitCodeRequestBean {
    private String appId;
    private String timestamp ;
    private String randomSeries ;
    private String cipherText;
}
