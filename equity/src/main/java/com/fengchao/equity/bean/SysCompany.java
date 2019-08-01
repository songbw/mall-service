package com.fengchao.equity.bean;

import java.util.Date;

public class SysCompany {
    private Long id;

    private String name;

    private String corporationName;

    private String corporationId;

    private String licenceUrl;

    private String phone;

    private String address;

    private String industry;

    private Date createTime;

    private Date updateTime;

    private String comments;

    private Integer status;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public void setCorporationId(String corporationId) {
        this.corporationId = corporationId;
    }

    public void setLicenceUrl(String licenceUrl) {
        this.licenceUrl = licenceUrl;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public String getCorporationId() {
        return corporationId;
    }

    public String getLicenceUrl() {
        return licenceUrl;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getIndustry() {
        return industry;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getComments() {
        return comments;
    }

    public Integer getStatus() {
        return status;
    }
}
