package com.fengchao.pingan.config;

import com.fengchao.pingan.bean.PingAnConfigBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "pingan")
public class PingAnClientConfig {

    private Map<String, PingAnConfigBean> region;

    private String wkAppId;

    private String wkAppSecret ;

    private String wkBaseUrl ;

    public Map<String, PingAnConfigBean> getRegion() {
        return region;
    }

    public void setRegion(Map<String, PingAnConfigBean> region) {
        this.region = region;
    }

    public String getWkAppId() {
        return wkAppId;
    }

    public void setWkAppId(String wkAppId) {
        this.wkAppId = wkAppId;
    }

    public String getWkAppSecret() {
        return wkAppSecret;
    }

    public void setWkAppSecret(String wkAppSecret) {
        this.wkAppSecret = wkAppSecret;
    }

    public String getWkBaseUrl() {
        return wkBaseUrl;
    }

    public void setWkBaseUrl(String wkBaseUrl) {
        this.wkBaseUrl = wkBaseUrl;
    }
}
