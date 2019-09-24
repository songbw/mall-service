package com.fengchao.order.bean;

import java.math.BigDecimal;
import java.util.Date;

public class BalanceDetail {
    private Integer id;

    private Integer balanceId;

    private String openId;

    private Integer orderId;

    private String orderNos;

    private BigDecimal saleAmount;

    private Integer type;

    private Integer status;

    private Date createdAt;

    private Date updatedAt;

    private String iAppId;

    private String tAppId;

    private String merchantNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Integer balanceId) {
        this.balanceId = balanceId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNos() {
        return orderNos;
    }

    public void setOrderNos(String orderNos) {
        this.orderNos = orderNos == null ? null : orderNos.trim();
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getiAppId() {
        return iAppId;
    }

    public void setiAppId(String iAppId) {
        this.iAppId = iAppId == null ? null : iAppId.trim();
    }

    public String gettAppId() {
        return tAppId;
    }

    public void settAppId(String tAppId) {
        this.tAppId = tAppId == null ? null : tAppId.trim();
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo == null ? null : merchantNo.trim();
    }
}