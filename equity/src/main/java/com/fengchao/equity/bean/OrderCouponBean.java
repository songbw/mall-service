package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCouponBean {
    private Integer couponId;
    private String userCouponCode;
    private List<OrderBean> orders;
}
