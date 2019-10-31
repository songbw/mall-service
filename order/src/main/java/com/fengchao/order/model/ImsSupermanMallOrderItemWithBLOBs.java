package com.fengchao.order.model;

public class ImsSupermanMallOrderItemWithBLOBs extends ImsSupermanMallOrderItem {
    private String sku;

    private String extend;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend == null ? null : extend.trim();
    }
}