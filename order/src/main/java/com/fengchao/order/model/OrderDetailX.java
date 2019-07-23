package com.fengchao.order.model;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDetailX {
    private Integer id;

    private Integer orderId;

    private String subOrderId;

    private String skuId;

    private Integer num;

    private BigDecimal unitPrice;

    private BigDecimal salePrice;

    private String image;

    private String name;

    private String model;

    private Integer status;

    private Integer promotionId;

    private BigDecimal promotionDiscount;

    private Date createdAt;

    private Date updatedAt;

    private String logisticsId;

    private String logisticsContent;

    private String comCode;

    private String mpu;

    private Integer merchantId;

    private Integer skuCouponDiscount ;

    private String category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
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

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(String logisticsId) {
        this.logisticsId = logisticsId;
    }

    public String getLogisticsContent() {
        return logisticsContent;
    }

    public void setLogisticsContent(String logisticsContent) {
        this.logisticsContent = logisticsContent;
    }

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public BigDecimal getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(BigDecimal promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public String getMpu() {
        return mpu;
    }

    public void setMpu(String mpu) {
        this.mpu = mpu;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
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
        this.category = category;
    }
}