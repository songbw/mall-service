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
     *  "name": "balance", // 惠民商城余额
     *  "name": "card", // 惠民优选卡
     *  "name": "woa", // 惠民商城联机账户
     *  "name": "bank", // 中投快捷支付
     *  "name": "pingan", // 惠民商城平安统一支付
     *  "name": "fcwxh5", // 凤巢微信H5支付
     *  "name": "fcalipay", // 凤巢支付宝H5支付
     *  "name": "fcwx", // 凤巢微信公众号支付
     *  "name": "fcwxxcx", // 凤巢微信小程序支付
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
     * 交易实际金额 单位 分
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

    /**
     * 卡号或OpenID
     */
    private String cardNo;

    /**
     * 手机号
     */
    private String payer;

    /**
     * tradeType
     */
    private String tradeType;

    /**
     * appId
     */
    private String appId;

}
