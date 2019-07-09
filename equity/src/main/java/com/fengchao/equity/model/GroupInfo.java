package com.fengchao.equity.model;

import java.util.Date;

public class GroupInfo {
    private Long id;

    private String name;

    private String skuId;

    private Date effectiveStartDate;

    private Date effectiveEndDate;

    private Integer groupBuyingPrice;

    private Integer groupBuyingQuantity;

    private Integer groupMemberQuantity;

    private Integer limitedPerMember;

    private String description;

    private Short groupStatus;

    private Short istatus;

    private Long operator;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    public Date getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(Date effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public Date getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(Date effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public Integer getGroupBuyingPrice() {
        return groupBuyingPrice;
    }

    public void setGroupBuyingPrice(Integer groupBuyingPrice) {
        this.groupBuyingPrice = groupBuyingPrice;
    }

    public Integer getGroupBuyingQuantity() {
        return groupBuyingQuantity;
    }

    public void setGroupBuyingQuantity(Integer groupBuyingQuantity) {
        this.groupBuyingQuantity = groupBuyingQuantity;
    }

    public Integer getGroupMemberQuantity() {
        return groupMemberQuantity;
    }

    public void setGroupMemberQuantity(Integer groupMemberQuantity) {
        this.groupMemberQuantity = groupMemberQuantity;
    }

    public Integer getLimitedPerMember() {
        return limitedPerMember;
    }

    public void setLimitedPerMember(Integer limitedPerMember) {
        this.limitedPerMember = limitedPerMember;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Short getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(Short groupStatus) {
        this.groupStatus = groupStatus;
    }

    public Short getIstatus() {
        return istatus;
    }

    public void setIstatus(Short istatus) {
        this.istatus = istatus;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
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