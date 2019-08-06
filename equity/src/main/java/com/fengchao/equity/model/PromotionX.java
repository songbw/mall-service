package com.fengchao.equity.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class PromotionX {
    private Integer id;

    private String name;

    private String tag;

//    private Integer promotionType;

    private Integer status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endDate;

    private Date createdDate;

    private Integer discountType;

    private Long promotionTypeId;

    private List<PromotionMpuX> promotionSkus;

    private List<PromotionSchedule> promotionSchedules;

    public PromotionX() {
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

//    public Integer getPromotionType() {
//        return promotionType;
//    }
//
//    public void setPromotionType(Integer promotionType) {
//        this.promotionType = promotionType;
//    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<PromotionMpuX> getPromotionSkus() {
        return promotionSkus;
    }

    public void setPromotionSkus(List<PromotionMpuX> promotionSkus) {
        this.promotionSkus = promotionSkus;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Long getPromotionTypeId() {
        return promotionTypeId;
    }

    public void setPromotionTypeId(Long promotionTypeId) {
        this.promotionTypeId = promotionTypeId;
    }

    public List<PromotionSchedule> getPromotionSchedules() {
        return promotionSchedules;
    }

    public void setPromotionSchedules(List<PromotionSchedule> promotionSchedules) {
        this.promotionSchedules = promotionSchedules;
    }
}