package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitCodeRequestBean {
    private String appId = "ea70244ca3604a4ebc1c2fd8e48756d5";
    private String timestamp ;
    private String randomSeries ;
    private String cipherText;
}
