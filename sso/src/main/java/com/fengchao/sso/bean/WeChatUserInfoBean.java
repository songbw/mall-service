package com.fengchao.sso.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/12/13 12:00
 */
@Getter
@Setter
public class WeChatUserInfoBean {
    private String openid ;
    private String nickname ;
    private String sex ;
    private String province ;
    private String city ;
    private String country ;
    private String headimgurl ;
    private String privilege ;
    private String unionid ;
}
