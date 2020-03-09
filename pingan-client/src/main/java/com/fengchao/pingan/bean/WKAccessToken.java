package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author song
 * @2020-02-26 4:16 下午
 **/
@Setter
@Getter
public class WKAccessToken {
    private String access_token ;
    private String refresh_token  ;
    private boolean expired ;
    private String scope ;
    private String expiration ;
    private String token_type ;
    private String expires_in ;
}
