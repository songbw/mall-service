package com.fengchao.pingan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pingan")
public class PingAnClientConfig {

    private String authBasePath;

    private String notifyUrl ;

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
}
