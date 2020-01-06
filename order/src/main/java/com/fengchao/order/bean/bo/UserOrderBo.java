package com.fengchao.order.bean.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用户单(支付单)
 */
@Setter
@Getter
public class UserOrderBo {

    /**
     * 支付单号
     */
    private String paymentNo;

    /**
     * 用户单号
     */
    private String userOrderNo;

    /**
     * 商户单
     */
    private List<OrdersBo> merchantOrderList;


}
