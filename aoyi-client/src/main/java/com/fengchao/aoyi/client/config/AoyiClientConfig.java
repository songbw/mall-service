package com.fengchao.aoyi.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aoyi")
public class AoyiClientConfig {

    private String gatBackUrl;

    private String gatUrl;

    private String fusionBaseUrl ;
    private String fusionAppKey ;
    private String fusionToken ;
    private String fusionKioskPath ;
    private String fusionSoltPath ;

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

    public String getFusionBaseUrl() {
        return fusionBaseUrl;
    }

    public void setFusionBaseUrl(String fusionBaseUrl) {
        this.fusionBaseUrl = fusionBaseUrl;
    }

    public String getFusionAppKey() {
        return fusionAppKey;
    }

    public void setFusionAppKey(String fusionAppKey) {
        this.fusionAppKey = fusionAppKey;
    }

    public String getFusionToken() {
        return fusionToken;
    }

    public void setFusionToken(String fusionToken) {
        this.fusionToken = fusionToken;
    }

    public String getFusionKioskPath() {
        return fusionKioskPath;
    }

    public void setFusionKioskPath(String fusionKioskPath) {
        this.fusionKioskPath = fusionKioskPath;
    }

    public String getFusionSoltPath() {
        return fusionSoltPath;
    }

    public void setFusionSoltPath(String fusionSoltPath) {
        this.fusionSoltPath = fusionSoltPath;
    }
}
