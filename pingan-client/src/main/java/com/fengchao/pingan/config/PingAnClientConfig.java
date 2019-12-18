package com.fengchao.pingan.config;

import com.fengchao.pingan.bean.PingAnConfigBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "pingan")
public class PingAnClientConfig {

    private Map<String, PingAnConfigBean> region;

    public Map<String, PingAnConfigBean> getRegion() {
        return region;
    }

    public void setRegion(Map<String, PingAnConfigBean> region) {
        this.region = region;
    }
}
