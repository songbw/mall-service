package com.fengchao.pingan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pingan")
public class PingAnClientConfig {

    private String authBasePath;

    public String getAuthBasePath() {
        return authBasePath;
    }

    public void setAuthBasePath(String authBasePath) {
        this.authBasePath = authBasePath;
    }
}
