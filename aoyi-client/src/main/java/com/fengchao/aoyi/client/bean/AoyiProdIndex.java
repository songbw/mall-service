package com.fengchao.aoyi.client.bean;


public class AoyiProdIndex {
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
    // 上下架状态 1：已上架；0：已下架
    private String state;

    private String price;

    private String sprice;

    private String imagesUrl;

    private String introductionUrl;

    private String categoryName;

    private String introduction;

    private String prodParams;

    private String imageExtend;

    private String imagesUrlExtend;

    private String introductionUrlExtend;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getProdParams() {
        return prodParams;
    }

    public void setProdParams(String prodParams) {
        this.prodParams = prodParams;
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
}