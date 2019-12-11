package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/12/10 14:04
 */
@Setter
@Getter
public class PingAnConfigBean {
    private String authBasePath;
    private String payBasePath;
    private String notifyUrl ;
    private String appId;
    private String appKey ;
    private String payAppId;
    private String payAppKey ;
    private String payMerchantNo;
}