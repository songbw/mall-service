package com.fengchao.product.aoyi.constants;

/**
 * @Author tom
 * @Date 19-8-27 上午10:59
 *
 * 上下架状态 -1:初始状态；0：下架；1：上架
 */
public enum CategoryClassEnum {

    LEVEL1(1, "1级品类"),
    LEVEL2(2, "2级品类"),
    LEVEL3(3, "3级品类");

    private int value;

    private String desc;

    CategoryClassEnum(int value, String desc) {
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
