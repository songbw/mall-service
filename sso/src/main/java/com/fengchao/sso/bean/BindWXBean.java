package com.fengchao.sso.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/11/27 14:47
 */
@Setter
@Getter
public class BindWXBean {
    private String telephone ;
    private String openId ;
    private String code ;
    private String appSrc;
}
