package com.fengchao.pingan.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PinganConfig {
    @Value("${pingan.baseUrl}")
    private String baseUrl;
    @Value("${pingan.paymentOrder}")
    private String paymentOrder;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(String paymentOrder) {
        this.paymentOrder = paymentOrder;
    }
}
