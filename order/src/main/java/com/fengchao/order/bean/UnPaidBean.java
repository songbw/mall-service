package com.fengchao.order.bean;

import com.fengchao.order.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author songbw
 * @date 2019/11/25 14:53
 */
@Setter
@Getter
public class UnPaidBean {
    //订单优惠券
    private OrderCouponBean coupon ;
    private Float saleAmount;
    private Float servFee;
    private List<Order> ordersList ;
    private String orderNos;
}
