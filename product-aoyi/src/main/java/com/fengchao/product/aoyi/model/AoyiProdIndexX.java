package com.fengchao.product.aoyi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AoyiProdIndexX implements Serializable {
    private Integer id;

    private String skuid;

    private String brand;

    private String category;

    private String image;

    private String model;

    private String name;

    private String weight;

    private String upc;

    private String saleunit;

    private String state;

    private String price;

    private String sprice;

    private String imagesUrl;

    private String introductionUrl;

    private String imageExtend;

    private String imagesUrlExtend;

    private String introductionUrlExtend;

    private Integer merchantId ;

    private Integer inventory ;

    private Integer brandId ;

    private String mpu;

    private Date createdAt;

    private Date updatedAt;

    private List<String> ztImage;

    private List<String> xqImage;

    private Date syncAt ;

    private Integer type ;

    private String compareUrl ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid == null ? null : skuid.trim();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc == null ? null : upc.trim();
    }

    public String getSaleunit() {
        return saleunit;
    }

    public void setSaleunit(String saleunit) {
        this.saleunit = saleunit == null ? null : saleunit.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getSprice() {
        return sprice;
    }

    public void setSprice(String sprice) {
        this.sprice = sprice == null ? null : sprice.trim();
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl == null ? null : imagesUrl.trim();
    }

    public String getIntroductionUrl() {
        return introductionUrl;
    }

    public void setIntroductionUrl(String introductionUrl) {
        this.introductionUrl = introductionUrl == null ? null : introductionUrl.trim();
    }

    public String getImageExtend() {
        return imageExtend;
    }

    public void setImageExtend(String imageExtend) {
        this.imageExtend = imageExtend;
    }

    public String getImagesUrlExtend() {
        return imagesUrlExtend;
    }

    public void setImagesUrlExtend(String imagesUrlExtend) {
        this.imagesUrlExtend = imagesUrlExtend;
    }

    public String getIntroductionUrlExtend() {
        return introductionUrlExtend;
    }

    public void setIntroductionUrlExtend(String introductionUrlExtend) {
        this.introductionUrlExtend = introductionUrlExtend;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getMpu() {
        return mpu;
    }

    public void setMpu(String mpu) {
        this.mpu = mpu;
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

    public List<String> getZtImage() {
        return ztImage;
    }

    public void setZtImage(List<String> ztImage) {
        this.ztImage = ztImage;
    }

    public List<String> getXqImage() {
        return xqImage;
    }

    public void setXqImage(List<String> xqImage) {
        this.xqImage = xqImage;
    }

    public Date getSyncAt() {
        return syncAt;
    }

    public void setSyncAt(Date syncAt) {
        this.syncAt = syncAt;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCompareUrl() {
        return compareUrl;
    }

    public void setCompareUrl(String compareUrl) {
        this.compareUrl = compareUrl;
    }
}