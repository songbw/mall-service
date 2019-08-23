package com.fengchao.sso.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sso")
// @Component
public class SSOConfiguration  {

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
