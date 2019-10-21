package com.fengchao.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

@ConfigurationProperties(prefix = "gateway")
public class GatewayConfig implements Serializable {

    private List<String> noAuthorization;

    public List<String> getNoAuthorization() {
        return noAuthorization;
    }

    public void setNoAuthorization(List<String> noAuthorization) {
        this.noAuthorization = noAuthorization;
    }
}
