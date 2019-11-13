package com.fengchao.pingan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pingan")
public class PingAnClientConfig {

    private String authBasePath;

    private String notifyUrl ;

    private String appId;

    private String appKey ;

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
}
