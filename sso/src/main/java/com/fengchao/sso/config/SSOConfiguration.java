package com.fengchao.sso.config;

import com.fengchao.sso.bean.SSOConfigBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@ConfigurationProperties(prefix = "sso")
// @Component
public class SSOConfiguration  implements Serializable {

    private Map<String, SSOConfigBean> region ;

    public Map<String, SSOConfigBean> getRegion() {
        return region;
    }

    public void setRegion(Map<String, SSOConfigBean> region) {
        this.region = region;
    }
}
