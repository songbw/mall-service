package com.fengchao.order.constants;

/**
 * 支付状态
 * 10初始创建订单
 * 1下单成功，等待支付。
 * 2支付中，3超时未支付  4支付失败  5支付成功
 * 11支付成功，记账也成功   12支付成功，记账失败  14退款失败，15订单已退款
 */
public enum PaymentStatusEnum {

    CREATED(10, "初始创建订单"),
    WAITING_FOR_PAY(1, "下单成功，等待支付。"),
    PAY_INPROCESS(2, "支付中，"),
    PAY_TIMEOUT(3, "超时未支付"),
    PAY_FAIL(4, "支付失败"),
    PAY_SUCCESS(5, "支付成功"),
    PAY_SUCCESS_RECORD_SUCCESS(11, "支付成功，记账也成功"),
    PAY_SUCCESS_RECORD_FALI(12, "支付成功，记账失败"),
    REFUND_FAIL(14, "退款失败，"),
    REFUND_COMPLETE(15, "订单已退款");



    private int value;

    private String desc;

    PaymentStatusEnum(int value, String desc) {
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
