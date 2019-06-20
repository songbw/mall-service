package com.fengchao.order.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCouponBean {
    private Integer id = 0; // 优惠券ID
    private List<OrderCouponMerchantBean> merchants = new ArrayList<>();
}
