package com.fengchao.product.aoyi.constants;

/**
 * @Author tom
 * @Date 19-8-27 上午10:59
 * <p>
 * 上下架状态 -1:初始状态；0：下架；1：上架
 */
public enum ProductStatusEnum {

    INIT(-1, "初始状态"),
    PUT_ON(0, "下架"),
    PUT_OFF(1, "上架");

    private int value;

    private String desc;

    ProductStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据state获取相应的enum
     *
     * @param state
     * @return
     */
    public static ProductStatusEnum getProductStatusEnum(int state) {
        switch (state) {
            case -1:
                return INIT;
            case 0:
                return PUT_ON;
            case 1:
                return PUT_OFF;
            default:
                return null;
        }
    }
}
