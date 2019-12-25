package com.fengchao.order.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author songbw
 * @date 2019/12/24 15:16
 */
@Setter
@Getter
public class GoodsDetailBean {
    private String sku_id ;
    private String name ;
    private int quantity ;
    private BigDecimal good_price = new BigDecimal(0.00);
    private BigDecimal good_pay_amount  = new BigDecimal(0.00);
    private BigDecimal good_cost_amount  = new BigDecimal(0.00);

}
