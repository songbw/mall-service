package com.fengchao.product.aoyi.model;

public class StarSpu {
    private Integer id;

    private Integer brandId;

    private Integer categoryId;

    private Integer crossBorder;

    private String goodsAreaInfo;

    private Integer goodsAreaKbn;

    private String goodsCode;

    private Integer goodsIslimit;

    private Integer goosLimitNum;

    private String mainImgUrl;

    private String name;

    private String simpleDesc;

    private String spuId;

    private Integer status;

    private Integer transFee;

    private String videoUrl;

    private String detailInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCrossBorder() {
        return crossBorder;
    }

    public void setCrossBorder(Integer crossBorder) {
        this.crossBorder = crossBorder;
    }

    public String getGoodsAreaInfo() {
        return goodsAreaInfo;
    }

    public void setGoodsAreaInfo(String goodsAreaInfo) {
        this.goodsAreaInfo = goodsAreaInfo == null ? null : goodsAreaInfo.trim();
    }

    public Integer getGoodsAreaKbn() {
        return goodsAreaKbn;
    }

    public void setGoodsAreaKbn(Integer goodsAreaKbn) {
        this.goodsAreaKbn = goodsAreaKbn;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public Integer getGoodsIslimit() {
        return goodsIslimit;
    }

    public void setGoodsIslimit(Integer goodsIslimit) {
        this.goodsIslimit = goodsIslimit;
    }

    public Integer getGoosLimitNum() {
        return goosLimitNum;
    }

    public void setGoosLimitNum(Integer goosLimitNum) {
        this.goosLimitNum = goosLimitNum;
    }

    public String getMainImgUrl() {
        return mainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl == null ? null : mainImgUrl.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSimpleDesc() {
        return simpleDesc;
    }

    public void setSimpleDesc(String simpleDesc) {
        this.simpleDesc = simpleDesc == null ? null : simpleDesc.trim();
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId == null ? null : spuId.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTransFee() {
        return transFee;
    }

    public void setTransFee(Integer transFee) {
        this.transFee = transFee;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl == null ? null : videoUrl.trim();
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo == null ? null : detailInfo.trim();
    }
}