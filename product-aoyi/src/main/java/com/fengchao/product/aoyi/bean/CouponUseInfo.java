package com.fengchao.product.aoyi.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

public class CouponUseInfo {
    private Integer id;

    private Integer couponId;

    private String userCouponCode;

    private String userOpenId;

    private String code;

    private Date collectedTime;

    private Date consumedTime;

    private Integer orderId;

    private Integer status;

    private Integer deleteFlag;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CouponBean couponInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getUserCouponCode() {
        return userCouponCode;
    }

    public void setUserCouponCode(String userCouponCode) {
        this.userCouponCode = userCouponCode == null ? null : userCouponCode.trim();
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId == null ? null : userOpenId.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Date getCollectedTime() {
        return collectedTime;
    }

    public void setCollectedTime(Date collectedTime) {
        this.collectedTime = collectedTime;
    }

    public Date getConsumedTime() {
        return consumedTime;
    }

    public void setConsumedTime(Date consumedTime) {
        this.consumedTime = consumedTime;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public CouponBean getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(CouponBean couponInfo) {
        this.couponInfo = couponInfo;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}