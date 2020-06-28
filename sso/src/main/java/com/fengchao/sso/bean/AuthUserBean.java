package com.fengchao.sso.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AuthUserBean implements Serializable{
    private int realNameStatus;
    private int marry;
    private String mobileNo;
    private String mobile;
    private String loginName;
    private String sex;
    private String openId;
    private String headImg;
    private String userName;
    private String idcard;
    private String cardType;
    private String address;
    private String email;
    private String volk;
    private String birthday;
    private String birthPlace;
    private String degree;
    private String census;
    private String nickName;
    private String nickname;
    private String payId;
}
