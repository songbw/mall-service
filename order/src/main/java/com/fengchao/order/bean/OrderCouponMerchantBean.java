package com.fengchao.order.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCouponMerchantBean {
    private String merchantNo = "";
    private List<OrderSku> skus = new ArrayList<>();
}
