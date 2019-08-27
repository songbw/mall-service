package com.fengchao.aoyi.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aoyi")
public class AoyiClientConfig {

    private String gatBackUrl;

    private String gatUrl;

    public String getGatBackUrl() {
        return gatBackUrl;
    }

    public void setGatBackUrl(String gatBackUrl) {
        this.gatBackUrl = gatBackUrl;
    }

    public String getGatUrl() {
        return gatUrl;
    }

    public void setGatUrl(String gatUrl) {
        this.gatUrl = gatUrl;
    }
}
