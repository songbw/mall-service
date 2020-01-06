package com.fengchao.order.model;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDetail {
    private Integer id;

    private Integer merchantId;

    private Integer orderId;

    private String subOrderId;

    private String mpu;

    private String skuId;

    private Integer num;

    private Integer promotionId;

    private Float promotionDiscount;

    private BigDecimal salePrice;

    private BigDecimal unitPrice;

    private String image;

    private String model;

    private String name;

    private Integer status;

    private Date createdAt;

    private Date updatedAt;

    private String logisticsId;

    private String logisticsContent;

    private String comcode;

    private Integer skuCouponDiscount;

    private String category;

    private String remark;

    private Date completeTime;

    private BigDecimal checkedPrice;

    private BigDecimal sprice;

    private Integer productType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId == null ? null : subOrderId.trim();
    }

    public String getMpu() {
        return mpu;
    }

    public void setMpu(String mpu) {
        this.mpu = mpu == null ? null : mpu.trim();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public Float getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(Float promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId == null ? null : logisticsId.trim();
    }

    public String getLogisticsContent() {
        return logisticsContent;
    }

    public void setLogisticsContent(String logisticsContent) {
        this.logisticsContent = logisticsContent == null ? null : logisticsContent.trim();
    }

    public String getComcode() {
        return comcode;
    }

    public void setComcode(String comcode) {
        this.comcode = comcode == null ? null : comcode.trim();
    }

    public Integer getSkuCouponDiscount() {
        return skuCouponDiscount;
    }

    public void setSkuCouponDiscount(Integer skuCouponDiscount) {
        this.skuCouponDiscount = skuCouponDiscount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public BigDecimal getCheckedPrice() {
        return checkedPrice;
    }

    public void setCheckedPrice(BigDecimal checkedPrice) {
        this.checkedPrice = checkedPrice;
    }

    public BigDecimal getSprice() {
        return sprice;
    }

    public void setSprice(BigDecimal sprice) {
        this.sprice = sprice;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }
}