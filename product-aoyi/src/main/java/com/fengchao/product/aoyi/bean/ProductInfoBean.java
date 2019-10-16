package com.fengchao.product.aoyi.bean;

import com.fengchao.product.aoyi.model.AoyiProdIndex;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductInfoBean extends AoyiProdIndex implements Serializable {

    private List<PromotionInfoBean> promotion = new ArrayList<>();

    private List<CouponBean> coupon = new ArrayList<>();

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
}
