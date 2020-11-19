package com.fengchao.product.aoyi.model;

public class AoyiProdIndexWithBLOBs extends AoyiProdIndex {
    private String introduction;

    private String imagesUrl;

    private String introductionUrl;

    private String prodparams;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
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

    public String getProdparams() {
        return prodparams;
    }

    public void setProdparams(String prodparams) {
        this.prodparams = prodparams == null ? null : prodparams.trim();
    }
}