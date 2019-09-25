package com.fengchao.order.bean;

import com.fengchao.order.model.OrderBalance;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class BalanceMerchantBean implements Serializable {
    private String merchantNo ;
    private List<OrderBalance> skus;
    private float BalanceDiscount;
}
