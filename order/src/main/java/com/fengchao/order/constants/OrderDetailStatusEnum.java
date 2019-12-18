package com.fengchao.order.constants;

/**
 * 子订单状态
 * 0：已下单；1：待发货；2：已发货（15天后自动变为已完成）；3：已完成；4：已取消；5：已取消，申请售后
 */
public enum OrderDetailStatusEnum {

    ORDERED(0, "已下单"),
    TO_BE_DELIVERY(1, "待发货"),
    DELIVERED(2, "已发货"),
    COMPLETED(3, "已完成"),
    CANCEL(4, "已取消"),
    APPLY_REFUND(5, "已取消，申请售后");



    private int value;

    private String desc;

    OrderDetailStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
