package com.fengchao.sso.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@ConfigurationProperties(prefix = "sso")
// @Component
public class SSOConfiguration  implements Serializable {

    private String gatBackUrl;

    private String gatUrl;

    private String iAppId;

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

    public String getiAppId() {
        return iAppId;
    }

    public void setiAppId(String iAppId) {
        this.iAppId = iAppId;
    }
}
