package com.fengchao.order.rpc.extmodel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Author tom
 * @Date 19-10-23 下午7:51
 */
@Getter
@Setter
public class OrderPayMethodInfoBean {

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 支付订单号
     */
    private String orderNo;

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 联机账户订单号
     */
    private String tradeNo;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 用户自定义
     */
    private String remark;

    /**
     * 交易总金额
     */
    private String totalFee;

    /**
     * 交易实际金额
     */
    private String actPayFee;

    /**
     * 交易状态: 1: 成功, 2: 失败, 0: 新创建
     */
    private Integer status;

    /**
     * 交易时间
     */
    private String tradeDate;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 支付限制
     */
    private String limitPay;
}
