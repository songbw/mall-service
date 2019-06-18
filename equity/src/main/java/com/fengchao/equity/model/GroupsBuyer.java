package com.fengchao.equity.model;

import java.util.Date;

public class GroupsBuyer {
    private Integer id;

    private Integer groupId;

    private Date createdTime;

    private Date closingTime;

    private Integer groupingstatus;

    private String leaderopenid;

    private String leaderorderid;

    private String memberopenids;

    private String memberorderids;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Date closingTime) {
        this.closingTime = closingTime;
    }

    public Integer getGroupingstatus() {
        return groupingstatus;
    }

    public void setGroupingstatus(Integer groupingstatus) {
        this.groupingstatus = groupingstatus;
    }

    public String getLeaderopenid() {
        return leaderopenid;
    }

    public void setLeaderopenid(String leaderopenid) {
        this.leaderopenid = leaderopenid == null ? null : leaderopenid.trim();
    }

    public String getLeaderorderid() {
        return leaderorderid;
    }

    public void setLeaderorderid(String leaderorderid) {
        this.leaderorderid = leaderorderid == null ? null : leaderorderid.trim();
    }

    public String getMemberopenids() {
        return memberopenids;
    }

    public void setMemberopenids(String memberopenids) {
        this.memberopenids = memberopenids == null ? null : memberopenids.trim();
    }

    public String getMemberorderids() {
        return memberorderids;
    }

    public void setMemberorderids(String memberorderids) {
        this.memberorderids = memberorderids == null ? null : memberorderids.trim();
    }
}