package com.fengchao.statistics.model;

import java.util.Date;

public class MStatisticUser {
    private Long id;

    private Integer merchantId;

    private String merchantCode;

    private String merchantName;

    private Integer orderUserCount;

    private Integer refundUserCount;

    private Date statisticsDate;

    private Date statisticStartTime;

    private Date statisticEndTime;

    private Short periodType;

    private Short istatus;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode == null ? null : merchantCode.trim();
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName == null ? null : merchantName.trim();
    }

    public Integer getOrderUserCount() {
        return orderUserCount;
    }

    public void setOrderUserCount(Integer orderUserCount) {
        this.orderUserCount = orderUserCount;
    }

    public Integer getRefundUserCount() {
        return refundUserCount;
    }

    public void setRefundUserCount(Integer refundUserCount) {
        this.refundUserCount = refundUserCount;
    }

    public Date getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public Date getStatisticStartTime() {
        return statisticStartTime;
    }

    public void setStatisticStartTime(Date statisticStartTime) {
        this.statisticStartTime = statisticStartTime;
    }

    public Date getStatisticEndTime() {
        return statisticEndTime;
    }

    public void setStatisticEndTime(Date statisticEndTime) {
        this.statisticEndTime = statisticEndTime;
    }

    public Short getPeriodType() {
        return periodType;
    }

    public void setPeriodType(Short periodType) {
        this.periodType = periodType;
    }

    public Short getIstatus() {
        return istatus;
    }

    public void setIstatus(Short istatus) {
        this.istatus = istatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}