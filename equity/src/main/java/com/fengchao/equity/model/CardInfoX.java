package com.fengchao.equity.model;

import java.util.Date;
import java.util.List;

public class CardInfoX {
    private Integer id;

    private String name;

    private String amount;

    private Short type;

    private Integer effectiveDays;

    private Date createTime;

    private Date updateTime;

    private Short status;

    private Short isDelete;

    private String appId;

    private List<Integer> couponIds;

    private List<CardTicket> tickets;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Integer getEffectiveDays() {
        return effectiveDays;
    }

    public void setEffectiveDays(Integer effectiveDays) {
        this.effectiveDays = effectiveDays;
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

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Short isDelete) {
        this.isDelete = isDelete;
    }

    public List<Integer> getCouponIds() {
        return couponIds;
    }

    public void setCouponIds(List<Integer> couponIds) {
        this.couponIds = couponIds;
    }

    public List<CardTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<CardTicket> tickets) {
        this.tickets = tickets;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}