package com.fengchao.order.model;

import java.util.Date;

public class WeipinhuiAddress {
    private Long id;

    private String snCode;

    private String snPcode;

    private String snName;

    private String wphCode;

    private String wphPcode;

    private String wphName;

    private Integer level;

    private Short istatus;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode == null ? null : snCode.trim();
    }

    public String getSnPcode() {
        return snPcode;
    }

    public void setSnPcode(String snPcode) {
        this.snPcode = snPcode == null ? null : snPcode.trim();
    }

    public String getSnName() {
        return snName;
    }

    public void setSnName(String snName) {
        this.snName = snName == null ? null : snName.trim();
    }

    public String getWphCode() {
        return wphCode;
    }

    public void setWphCode(String wphCode) {
        this.wphCode = wphCode == null ? null : wphCode.trim();
    }

    public String getWphPcode() {
        return wphPcode;
    }

    public void setWphPcode(String wphPcode) {
        this.wphPcode = wphPcode == null ? null : wphPcode.trim();
    }

    public String getWphName() {
        return wphName;
    }

    public void setWphName(String wphName) {
        this.wphName = wphName == null ? null : wphName.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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