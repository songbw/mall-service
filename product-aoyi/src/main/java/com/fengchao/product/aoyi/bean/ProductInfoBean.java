package com.fengchao.product.aoyi.bean;

import com.fengchao.product.aoyi.model.AoyiProdIndex;
import com.fengchao.product.aoyi.model.AoyiProdIndexXWithBLOBs;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductInfoBean extends AoyiProdIndexXWithBLOBs implements Serializable {

    private List<PromotionInfoBean> promotion = new ArrayList<>();

    private List<CouponBean> coupon = new ArrayList<>();

    private String categoryName ;

    public List<PromotionInfoBean> getPromotion() {
        return promotion;
    }

    public void setPromotion(List<PromotionInfoBean> promotion) {
        this.promotion = promotion;
    }

    public List<CouponBean> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<CouponBean> coupon) {
        this.coupon = coupon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
