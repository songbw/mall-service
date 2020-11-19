package com.fengchao.product.aoyi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fengchao.product.aoyi.bean.StarSkuBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AoyiProdIndexX extends AoyiProdIndexWithBLOBs implements Serializable {

    private String imageExtend;

    private String imagesUrlExtend;

    private String introductionUrlExtend;

    private List<String> ztImage;

    private List<String> xqImage;

    /**
     * spu属性
     */
    private List<StarProperty> properties ;

    /**
     * sku信息
     */
    private List<StarSkuBean> skuList ;

    private StarSkuBean starSku ;

    private BigDecimal renterPrice ;

    private List<AppSkuPrice> appSkuPriceList ;

    private List<AppSkuState> appSkuStateList ;

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

    public List<StarProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<StarProperty> properties) {
        this.properties = properties;
    }

    public List<com.fengchao.product.aoyi.bean.StarSkuBean> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<com.fengchao.product.aoyi.bean.StarSkuBean> skuList) {
        this.skuList = skuList;
    }

    public StarSkuBean getStarSku() {
        return starSku;
    }

    public void setStarSku(StarSkuBean starSku) {
        this.starSku = starSku;
    }

    public BigDecimal getRenterPrice() {
        return renterPrice;
    }

    public void setRenterPrice(BigDecimal renterPrice) {
        this.renterPrice = renterPrice;
    }

    public List<AppSkuPrice> getAppSkuPriceList() {
        return appSkuPriceList;
    }

    public void setAppSkuPriceList(List<AppSkuPrice> appSkuPriceList) {
        this.appSkuPriceList = appSkuPriceList;
    }

    public List<AppSkuState> getAppSkuStateList() {
        return appSkuStateList;
    }

    public void setAppSkuStateList(List<AppSkuState> appSkuStateList) {
        this.appSkuStateList = appSkuStateList;
    }
}