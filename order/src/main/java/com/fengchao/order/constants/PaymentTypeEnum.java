package com.fengchao.order.constants;

import lombok.extern.slf4j.Slf4j;

/**
 * 支持的支付方式
 *
 * @author zp
 */
@Slf4j
public enum PaymentTypeEnum {

    BALANCE("balance", "惠民商城余额", "sync", "sync"),
    CARD("card", "惠民优选卡", "sync", "sync"),
    WOA("woa", "惠民商城联机账户", "sync", "sync"),
    BANK("bank", "中投快捷支付", "sync", "async"),
    PINGAN("pingan", "惠民商城平安统一支付", "async", "sync"),
    FCWXH5("fcwxh5", "凤巢微信H5支付", "async", "async"),
    FCALIPAY("fcalipay", "凤巢支付宝H5支付", "async", "async"),
    FCWX("fcwx", "凤巢微信公众号支付", "async", "async"),
    FCWXXCX("fcwxxcx", "凤巢微信小程序支付", "async", "async");

    private String name;
    private String desc;
    private String pay;
    private String refund;

    PaymentTypeEnum(String name, String desc, String pay, String refund) {
        this.name = name;
        this.desc = desc;
        this.pay = pay;
        this.refund = refund;
    }

    /**
     *
     * @param name
     * @return
     */
    public static PaymentTypeEnum getPaymentTypeEnumByName(String name) {
        switch (name) {
            case "balance":
                return BALANCE;
            case "card":
                return CARD;
            case "woa":
                return WOA;
            case "bank":
                return BANK;
            case "pingan":
                return PINGAN;
            case "fcwxh5":
                return FCWXH5;
            case "fcalipay":
                return FCALIPAY;
            case "fcwx":
                return FCWX;
            case "fcwxxcx":
                return FCWXXCX;
            default:
                log.warn("根据name没有找到定义的支付方式枚举值: {}", name);
                return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getPay() {
        return pay;
    }

    public String getRefund() {
        return refund;
    }

    public String getDesc() {
        return desc;
    }
}
