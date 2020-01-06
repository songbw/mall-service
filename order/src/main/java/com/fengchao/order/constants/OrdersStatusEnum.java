package com.fengchao.order.constants;

/**
 * 订单状态
 * -1:删除；0：待付款；1：待收货；2：已完成；3：已取消
 */
public enum OrdersStatusEnum {

    INVALID(-1, "INVALID", "已下单"),
    NON_PAYMENT(0, "NON_PAYMENT", "待付款"),
    DELIVERED(2, "DELIVERED", "待收货"),
    COMPLETED(3, "COMPLETED", "已完成"),
    CANCEL(4, "CANCEL", "已取消");



    private int code;

    private String value;

    private String desc;

    OrdersStatusEnum(int code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
