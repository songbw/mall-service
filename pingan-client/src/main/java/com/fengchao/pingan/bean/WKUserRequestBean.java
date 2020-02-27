package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author song
 * @2020-02-26 4:13 下午
 **/
@Setter
@Getter
public class WKUserRequestBean {
    private String access_token  ;
    private String openid ;
    private String lang = "zh-cn" ;
}
