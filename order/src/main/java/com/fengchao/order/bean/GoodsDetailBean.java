package com.fengchao.order.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author songbw
 * @date 2020/3/27 18:54
 */
@Setter
@Getter
public class GoodsDetailBean {
    private String sku_id ;
    private String name ;
    private int quantity ;
    private BigDecimal good_price ;
    private BigDecimal good_pay_amount ;
    private BigDecimal good_cost_amount ;
}
