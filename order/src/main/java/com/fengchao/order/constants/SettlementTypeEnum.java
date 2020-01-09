package com.fengchao.order.constants;

import lombok.extern.slf4j.Slf4j;

/**
 * 结算类型（0：普通类结算， 1：秒杀类结算， 2：精品类结算）
 * <p>
 * <p>
 * 目前有
 */
@Slf4j
public enum SettlementTypeEnum {

    UNKNOWN(-1, "UNKNOWN", "未知"),
    NORMAL(0, "NORMAL", "普通"),
    SECKILL(1, "SECKILL", "秒杀"),
    PREMIUM(2, "PREMIUM", "精品");


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

    /**
     *
     * @param code
     * @return
     */
    public static SettlementTypeEnum getSettlementTypeEnum(Integer code) {
        switch (code) {
            case 0:
                return NORMAL;
            case 1:
                return SECKILL;
            case 2:
                return PREMIUM;

            default:
                log.warn("未获取到有效的SettlementTypeEnum枚举值 code:{}", code);
                return UNKNOWN;
        }
    }

}
