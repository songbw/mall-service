package com.fengchao.product.aoyi.model;

public class AoyiProdIndexWithBLOBs extends AoyiProdIndex {
    private String introduction;

    private String prodparams;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

    public String getProdparams() {
        return prodparams;
    }

    public void setProdparams(String prodparams) {
        this.prodparams = prodparams == null ? null : prodparams.trim();
    }
}