package com.fengchao.statistics.model;

import java.util.Date;

public class PromotionOverview {
    private Long id;

    private Integer promotionId;

    private String promotionName;

    private float orderPaymentAmount;

    private Date statisticsDate;

    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName == null ? null : promotionName.trim();
    }

    public float getOrderPaymentAmount() {
        return orderPaymentAmount;
    }

    public void setOrderPaymentAmount(float orderPaymentAmount) {
        this.orderPaymentAmount = orderPaymentAmount;
    }

    public Date getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}