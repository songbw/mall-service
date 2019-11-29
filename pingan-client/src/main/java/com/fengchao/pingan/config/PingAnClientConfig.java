package com.fengchao.pingan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pingan")
public class PingAnClientConfig {
    private String authBasePath;
    private String payBasePath;
    private String notifyUrl ;
    private String appId;
    private String appKey ;
    private String payAppId;
    private String payAppKey ;
    private String payMerchantNo;

    public String getAuthBasePath() {
        return authBasePath;
    }

    public void setAuthBasePath(String authBasePath) {
        this.authBasePath = authBasePath;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getPayMerchantNo() {
        return payMerchantNo;
    }

    public void setPayMerchantNo(String payMerchantNo) {
        this.payMerchantNo = payMerchantNo;
    }

    public String getPayBasePath() {
        return payBasePath;
    }

    public void setPayBasePath(String payBasePath) {
        this.payBasePath = payBasePath;
    }

    public String getPayAppId() {
        return payAppId;
    }

    public void setPayAppId(String payAppId) {
        this.payAppId = payAppId;
    }

    public String getPayAppKey() {
        return payAppKey;
    }

    public void setPayAppKey(String payAppKey) {
        this.payAppKey = payAppKey;
    }
}
