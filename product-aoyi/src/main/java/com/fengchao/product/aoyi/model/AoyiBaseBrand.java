package com.fengchao.product.aoyi.model;

import java.util.Date;

public class AoyiBaseBrand {
    private Integer brandId;

    private String brandName;

    private String brandCname;

    private String brandEname;

    private String brandFirstChar;

    private String brandLogo;

    private String siteUrl;

    private Byte sortOrder;

    private Boolean isShow;

    private Date addTime;

    private String brandDesc;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getBrandCname() {
        return brandCname;
    }

    public void setBrandCname(String brandCname) {
        this.brandCname = brandCname == null ? null : brandCname.trim();
    }

    public String getBrandEname() {
        return brandEname;
    }

    public void setBrandEname(String brandEname) {
        this.brandEname = brandEname == null ? null : brandEname.trim();
    }

    public String getBrandFirstChar() {
        return brandFirstChar;
    }

    public void setBrandFirstChar(String brandFirstChar) {
        this.brandFirstChar = brandFirstChar == null ? null : brandFirstChar.trim();
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo == null ? null : brandLogo.trim();
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl == null ? null : siteUrl.trim();
    }

    public Byte getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Byte sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getBrandDesc() {
        return brandDesc;
    }

    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc == null ? null : brandDesc.trim();
    }
}