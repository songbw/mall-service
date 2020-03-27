package com.fengchao.order.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songbw
 * @date 2019/12/25 16:48
 */
@Setter
@Getter
public class ThirdOrdersBean {
    private String third_sub_trade_no ;
    private BigDecimal third_sub_delivery_fee = new BigDecimal(0.00);
    private BigDecimal third_sub_total_amount = new BigDecimal(0.00);
    private BigDecimal third_sub_pay_amount = new BigDecimal(0.00);
    private BigDecimal third_sub_cost_amount = new BigDecimal(0.00);
    private List<GoodsDetailBean> goods_detail = new ArrayList<>();

}
