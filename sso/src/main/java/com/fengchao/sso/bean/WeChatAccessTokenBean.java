package com.fengchao.sso.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/12/13 10:49
 */
@Setter
@Getter
public class WeChatAccessTokenBean {
    private String access_token ;
    private String session_key ;
    private long expires_in ;
    private String refresh_token ;
    private String openid ;
    private String scope ;
}
