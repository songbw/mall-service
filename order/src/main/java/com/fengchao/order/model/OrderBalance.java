package com.fengchao.order.model;

import java.math.BigDecimal;
import java.util.Date;

public class OrderBalance {
    private Integer id;

    private Integer merchantId;

    private Integer orderId;

    private Integer balanceId;

    private String subOrderId;

    private String dprice;

    private String mpu;

    private Integer num;

    private BigDecimal unitCoinBalanceDiscount;

    private BigDecimal skuCoinBalanceDiscount;

    private Date createdAt;

    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Integer balanceId) {
        this.balanceId = balanceId;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId == null ? null : subOrderId.trim();
    }

    public String getDprice() {
        return dprice;
    }

    public void setDprice(String dprice) {
        this.dprice = dprice == null ? null : dprice.trim();
    }

    public String getMpu() {
        return mpu;
    }

    public void setMpu(String mpu) {
        this.mpu = mpu == null ? null : mpu.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getUnitCoinBalanceDiscount() {
        return unitCoinBalanceDiscount;
    }

    public void setUnitCoinBalanceDiscount(BigDecimal unitCoinBalanceDiscount) {
        this.unitCoinBalanceDiscount = unitCoinBalanceDiscount;
    }

    public BigDecimal getSkuCoinBalanceDiscount() {
        return skuCoinBalanceDiscount;
    }

    public void setSkuCoinBalanceDiscount(BigDecimal skuCoinBalanceDiscount) {
        this.skuCoinBalanceDiscount = skuCoinBalanceDiscount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}