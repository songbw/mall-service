package com.fengchao.freight.model;

import java.util.Date;
import java.util.List;

public class FreeShippingTemplateX {
    private Integer id;

    private String name;

    private Integer merchantId;

    private Boolean isDefault;

    private Integer mode;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private List<FreeShippingRegionsX>  regions;

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

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<FreeShippingRegionsX> getRegions() {
        return regions;
    }

    public void setRegions(List<FreeShippingRegionsX> regions) {
        this.regions = regions;
    }
}