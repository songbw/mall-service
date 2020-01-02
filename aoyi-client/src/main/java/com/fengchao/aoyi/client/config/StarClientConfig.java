package com.fengchao.aoyi.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author songbw
 * @date 2020/1/2 10:55
 */
@ConfigurationProperties(prefix = "star")
public class StarClientConfig {
    private String appKey;

    private String appSecret;

    private String baseUrl;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
