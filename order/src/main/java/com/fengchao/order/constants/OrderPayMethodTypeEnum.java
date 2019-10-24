package com.fengchao.order.constants;

/**
 * 订单支付方式
 *
 */
public enum OrderPayMethodTypeEnum {

    BALANCE(1, "BALANCE", "余额支付"),
    HUIMIN_CARD(2, "CARD", "惠民卡支付"),
    WOA(3, "WOA", "联机账户支付"),
    BANK(4, "BANK", "快捷支付");

    private int code;

    private String value;

    private String desc;

    OrderPayMethodTypeEnum(int code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
