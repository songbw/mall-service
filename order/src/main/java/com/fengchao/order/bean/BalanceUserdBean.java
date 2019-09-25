package com.fengchao.order.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class BalanceUserdBean implements Serializable {
    private BigDecimal balanceUsed;
    private List<BalanceMerchantBean> merchants ;
}
