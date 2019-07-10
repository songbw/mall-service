package com.fengchao.aggregation.model;

public class AggregationMpu {
    private Integer id;

    private Integer aggregationId;

    private Integer promotionId;

    private String mpu;

    private Integer type;

    private Integer level;

    private Integer skuIndex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAggregationId() {
        return aggregationId;
    }

    public void setAggregationId(Integer aggregationId) {
        this.aggregationId = aggregationId;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getMpu() {
        return mpu;
    }

    public void setMpu(String mpu) {
        this.mpu = mpu;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSkuIndex() {
        return skuIndex;
    }

    public void setSkuIndex(Integer skuIndex) {
        this.skuIndex = skuIndex;
    }
}