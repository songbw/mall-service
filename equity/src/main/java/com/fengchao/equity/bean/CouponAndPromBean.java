package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CouponAndPromBean {
    private String mpu ;
    private List<CouponBean> coupons;
    private List<PromotionInfoBean> promotions;
}
