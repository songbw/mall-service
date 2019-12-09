package com.fengchao.equity.model;

import java.util.Date;

public class Coupon {
    private Integer id;

    private String name;

    private String supplierMerchantId;

    private String supplierMerchantName;

    private Integer releaseTotal;

    private Integer releaseNum;

    private Date releaseStartDate;

    private Date releaseEndDate;

    private Integer status;

    private Date effectiveStartDate;

    private Date effectiveEndDate;

    private String excludeDates;

    private String description;

    private String url;

    private Date createDate;

    private String category;

    private String tags;

    private String imageUrl;

    private String code;

    private String rulesDescription;

    private Integer perLimited;

    private String scopes;

    private Integer scenarioType;

    private String categories;

    private String brands;

    private Integer collectType;

    private Integer points;

    private Integer customerType;

    private String users;

    private Integer couponType;

    private String couponRules;

    private String appId;

    private String couponMpus;

    private String couponSkus;

    private String excludeMpus;

    private String excludeSkus;

    public String getCouponMpus() {
        return couponMpus;
    }

    public void setCouponMpus(String couponMpus) {
        this.couponMpus = couponMpus == null ? null : couponMpus.trim();
    }

    public String getCouponSkus() {
        return couponSkus;
    }

    public void setCouponSkus(String couponSkus) {
        this.couponSkus = couponSkus == null ? null : couponSkus.trim();
    }

    public String getExcludeMpus() {
        return excludeMpus;
    }

    public void setExcludeMpus(String excludeMpus) {
        this.excludeMpus = excludeMpus == null ? null : excludeMpus.trim();
    }

    public String getExcludeSkus() {
        return excludeSkus;
    }

    public void setExcludeSkus(String excludeSkus) {
        this.excludeSkus = excludeSkus == null ? null : excludeSkus.trim();
    }
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

    public String getSupplierMerchantId() {
        return supplierMerchantId;
    }

    public void setSupplierMerchantId(String supplierMerchantId) {
        this.supplierMerchantId = supplierMerchantId == null ? null : supplierMerchantId.trim();
    }

    public String getSupplierMerchantName() {
        return supplierMerchantName;
    }

    public void setSupplierMerchantName(String supplierMerchantName) {
        this.supplierMerchantName = supplierMerchantName == null ? null : supplierMerchantName.trim();
    }

    public Integer getReleaseTotal() {
        return releaseTotal;
    }

    public void setReleaseTotal(Integer releaseTotal) {
        this.releaseTotal = releaseTotal;
    }

    public Integer getReleaseNum() {
        return releaseNum;
    }

    public void setReleaseNum(Integer releaseNum) {
        this.releaseNum = releaseNum;
    }

    public Date getReleaseStartDate() {
        return releaseStartDate;
    }

    public void setReleaseStartDate(Date releaseStartDate) {
        this.releaseStartDate = releaseStartDate;
    }

    public Date getReleaseEndDate() {
        return releaseEndDate;
    }

    public void setReleaseEndDate(Date releaseEndDate) {
        this.releaseEndDate = releaseEndDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getExcludeDates() {
        return excludeDates;
    }

    public void setExcludeDates(String excludeDates) {
        this.excludeDates = excludeDates == null ? null : excludeDates.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getRulesDescription() {
        return rulesDescription;
    }

    public void setRulesDescription(String rulesDescription) {
        this.rulesDescription = rulesDescription == null ? null : rulesDescription.trim();
    }

    public Integer getPerLimited() {
        return perLimited;
    }

    public void setPerLimited(Integer perLimited) {
        this.perLimited = perLimited;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes == null ? null : scopes.trim();
    }

    public Integer getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(Integer scenarioType) {
        this.scenarioType = scenarioType;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories == null ? null : categories.trim();
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands == null ? null : brands.trim();
    }

    public Integer getCollectType() {
        return collectType;
    }

    public void setCollectType(Integer collectType) {
        this.collectType = collectType;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users == null ? null : users.trim();
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public String getCouponRules() {
        return couponRules;
    }

    public void setCouponRules(String couponRules) {
        this.couponRules = couponRules == null ? null : couponRules.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }
}