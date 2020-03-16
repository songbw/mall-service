package com.fengchao.product.aoyi.constants;

/**
 * 怡亚通商品SKU上下架状态
 * @author songbw
 * @date 2020/3/16 10:54
 */
public enum StarSkuStatusEnum {

    PUT_ON(0, "已上架"),
    PUT_OFF(1, "已下架"),
    UN_KNOWN(999, "未知");

    private int value;

    private String desc;

    StarSkuStatusEnum(int value, String desc) {
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
    public static StarSkuStatusEnum getStarSkuStatusEnum(int state) {
        switch (state) {
            case 0:
                return PUT_ON;
            case 1:
                return PUT_OFF;
            default:
                return UN_KNOWN;
        }
    }

}
