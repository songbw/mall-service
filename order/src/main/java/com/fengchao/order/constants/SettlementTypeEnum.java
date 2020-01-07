package com.fengchao.order.constants;

import lombok.extern.slf4j.Slf4j;

/**
 * 结算类型（0：普通类结算， 1：秒杀类结算， 2：精品类结算）
 * <p>
 *
 *  目前有
 */
@Slf4j
public enum SettlementTypeEnum {

    UNKNOWN(-1, "UNKNOWN", "未知"),
    NORMAL(1, "NORMAL", "普通"),
    SECKILL(2, "SECKILL", "秒杀"),
    PREMIUM(3, "PREMIUM", "精品");


    private Integer code;
    private String value;
    private String desc;

    SettlementTypeEnum(int code, String value, String desc) {
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
