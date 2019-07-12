package com.fengchao.equity.model;

import java.util.Date;

public class GroupMember {
    private Long id;

    private Long groupInfoId;

    private Long groupTeamId;

    private String memberOpenId;

    private Short isSponser;

    private Long orderId;

    private Short memberStatus;

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

    public Long getGroupTeamId() {
        return groupTeamId;
    }

    public void setGroupTeamId(Long groupTeamId) {
        this.groupTeamId = groupTeamId;
    }

    public String getMemberOpenId() {
        return memberOpenId;
    }

    public void setMemberOpenId(String memberOpenId) {
        this.memberOpenId = memberOpenId == null ? null : memberOpenId.trim();
    }

    public Short getIsSponser() {
        return isSponser;
    }

    public void setIsSponser(Short isSponser) {
        this.isSponser = isSponser;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Short getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Short memberStatus) {
        this.memberStatus = memberStatus;
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