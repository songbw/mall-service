package com.fengchao.equity.model;

import java.util.Date;
import java.util.List;

public class PromotionScheduleX {

    private Integer id;

    private String schedule;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    private Integer istatus;

    private Integer promotionId;

    private List<PromotionMpuX> promotionMpus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule == null ? null : schedule.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public Integer getIstatus() {
        return istatus;
    }

    public void setIstatus(Integer istatus) {
        this.istatus = istatus;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public List<PromotionMpuX> getPromotionMpus() {
        return promotionMpus;
    }

    public void setPromotionMpus(List<PromotionMpuX> promotionMpus) {
        this.promotionMpus = promotionMpus;
    }
}