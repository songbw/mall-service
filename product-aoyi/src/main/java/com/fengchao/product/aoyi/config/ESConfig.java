package com.fengchao.product.aoyi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.Map;

@ConfigurationProperties(prefix = "es")
public class ESConfig implements Serializable {

    private String esIndex;

    private String esType;

    private Map<String, MerchantCodeBean> region;

    public String getEsIndex() {
        return esIndex;
    }

    public void setEsIndex(String esIndex) {
        this.esIndex = esIndex;
    }

    public String getEsType() {
        return esType;
    }

    public void setEsType(String esType) {
        this.esType = esType;
    }

    public Map<String, MerchantCodeBean> getRegion() {
        return region;
    }

    public void setRegion(Map<String, MerchantCodeBean> region) {
        this.region = region;
    }
}
