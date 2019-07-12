package com.fengchao.equity.model;

import java.util.Date;

public class GroupTeam {
    private Long id;

    private Long groupInfoId;

    private String sponserOpenId;

    private String mpuId;

    private Short teamStatus;

    private Integer teamExpiration;

    private Short istatus;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupInfoId() {
        return groupInfoId;
    }

    public void setGroupInfoId(Long groupInfoId) {
        this.groupInfoId = groupInfoId;
    }

    public String getSponserOpenId() {
        return sponserOpenId;
    }

    public void setSponserOpenId(String sponserOpenId) {
        this.sponserOpenId = sponserOpenId == null ? null : sponserOpenId.trim();
    }

    public String getMpuId() {
        return mpuId;
    }

    public void setMpuId(String mpuId) {
        this.mpuId = mpuId == null ? null : mpuId.trim();
    }

    public Short getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(Short teamStatus) {
        this.teamStatus = teamStatus;
    }

    public Integer getTeamExpiration() {
        return teamExpiration;
    }

    public void setTeamExpiration(Integer teamExpiration) {
        this.teamExpiration = teamExpiration;
    }

    public Short getIstatus() {
        return istatus;
    }

    public void setIstatus(Short istatus) {
        this.istatus = istatus;
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