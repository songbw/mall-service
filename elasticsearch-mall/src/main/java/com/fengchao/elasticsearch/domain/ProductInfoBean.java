package com.fengchao.elasticsearch.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductInfoBean extends AoyiProdIndex implements Serializable {

    private List<PromotionInfoBean> promotion = new ArrayList<>();

    private String categoryName ;

    public List<PromotionInfoBean> getPromotion() {
        return promotion;
    }

    public void setPromotion(List<PromotionInfoBean> promotion) {
        this.promotion = promotion;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
