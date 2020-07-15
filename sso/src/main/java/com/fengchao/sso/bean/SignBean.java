package com.fengchao.sso.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2020/7/14 9:36
 */
@Setter
@Getter
public class SignBean {
    private String appKey ;
    private String appId ;
    private String telephone;
    private String sign ;
    private String timestamp ;
}
