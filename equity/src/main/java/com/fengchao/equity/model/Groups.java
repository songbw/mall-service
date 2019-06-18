package com.fengchao.equity.model;

import java.math.BigDecimal;
import java.util.Date;

public class Groups {
    private Integer id;

    private String name;

    private String skuid;

    private String imageUrl;

    private Date createdTime;

    private Date effectiveStartDate;

    private Date effectiveEndDate;

    private BigDecimal groupBuyingPrice;

    private String groupBuyingQuantity;

    private Integer groupMemberQuantity;

    private Integer limitedPerMember;

    private String paymentExpiration;

    private String description;

    private Integer campaignStatus;

    private Integer groupTotal;

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

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid == null ? null : skuid.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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

    public BigDecimal getGroupBuyingPrice() {
        return groupBuyingPrice;
    }

    public void setGroupBuyingPrice(BigDecimal groupBuyingPrice) {
        this.groupBuyingPrice = groupBuyingPrice;
    }

    public String getGroupBuyingQuantity() {
        return groupBuyingQuantity;
    }

    public void setGroupBuyingQuantity(String groupBuyingQuantity) {
        this.groupBuyingQuantity = groupBuyingQuantity == null ? null : groupBuyingQuantity.trim();
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

    public String getPaymentExpiration() {
        return paymentExpiration;
    }

    public void setPaymentExpiration(String paymentExpiration) {
        this.paymentExpiration = paymentExpiration == null ? null : paymentExpiration.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getCampaignStatus() {
        return campaignStatus;
    }

    public void setCampaignStatus(Integer campaignStatus) {
        this.campaignStatus = campaignStatus;
    }

    public Integer getGroupTotal() {
        return groupTotal;
    }

    public void setGroupTotal(Integer groupTotal) {
        this.groupTotal = groupTotal;
    }
}