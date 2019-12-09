package com.fengchao.equity.model;

import java.util.Date;

public class PromotionInitialSchedule {
    private Integer id;

    private String initialSchedule;

    private Date createTime;

    private Date updateTime;

    private String appId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInitialSchedule() {
        return initialSchedule;
    }

    public void setInitialSchedule(String initialSchedule) {
        this.initialSchedule = initialSchedule == null ? null : initialSchedule.trim();
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }
}