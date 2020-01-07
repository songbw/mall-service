package com.fengchao.order.bean.bo;

import com.fengchao.order.rpc.extmodel.OrderPayMethodInfoBean;
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

    /**
     * 支付方式详情
     */
    private List<OrderPayMethodInfoBean> orderPayMethodInfoBeanList;

    /**
     * 退款方式详情
     */
    private List<OrderPayMethodInfoBean> refundMethodInfoBeanList;

}
