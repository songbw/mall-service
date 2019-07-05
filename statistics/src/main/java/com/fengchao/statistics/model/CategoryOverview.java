package com.fengchao.statistics.model;

import java.util.Date;

public class CategoryOverview {
    private Long id;

    private String code;

    private String name;

    private Long orderPaymentAmount;

    private Date statisticsDate;

    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getOrderPaymentAmount() {
        return orderPaymentAmount;
    }

    public void setOrderPaymentAmount(Long orderPaymentAmount) {
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