package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author song
 * @2020-02-26 4:13 下午
 **/
@Setter
@Getter
public class WKTokenRequestBean {
    private String grant_type = "client_credential"  ;
    private String appid ;
    private String secret;
}
