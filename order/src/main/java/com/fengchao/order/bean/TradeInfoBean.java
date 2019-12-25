package com.fengchao.order.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songbw
 * @date 2019/12/24 15:14
 */
@Setter
@Getter
public class TradeInfoBean {
    private String third_trade_no ;
    private BigDecimal third_total_amount = new BigDecimal(0.00);
    private BigDecimal third_pay_amount = new BigDecimal(0.00);
    private BigDecimal third_cost_amount = new BigDecimal(0.00);
    private int is_third_orders = 2 ;
    private List<GoodsDetailBean> goods_detail;
    private List<ThirdOrdersBean> third_orders = new ArrayList<>();

}
