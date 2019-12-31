package com.fengchao.order.constants;

import lombok.extern.slf4j.Slf4j;

/**
 * 开票类型
 * <p>
 * 目前发票类型包括
 * 1. "balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户 这三种支付方式的发票
 * 2. bank 中投快捷支付的发票
 */
@Slf4j
public enum ReceiptTypeEnum {

    UNKNOWN(0, "UNKNOWN", "未知"),
    BALANCE(1, "BALANCE", "balance 支付方式的发票"), // "balance" 惠民商城余额;  "card" 惠民优选卡; "woa" 惠民商城联机账户
    CARD(2, "CARD", "card 支付方式的发票"),
    WOA(3, "WOA", "woa 支付方式的发票"),
    BANK(4, "BANK", "中投快捷支付的发票");


    private Integer code;
    private String value;
    private String desc;

    ReceiptTypeEnum(int code, String value, String desc) {
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
    public static ReceiptTypeEnum getReceiptTypeEnum(int code) {
        switch (code) {
            case 1:
                return BALANCE;
            case 2:
                return CARD;
            case 3:
                return WOA;
            case 4:
                return BANK;
            default:
                log.warn("未获取到有效的ReceiptTypeEnum枚举值 code:{}", code);

                return UNKNOWN;
        }
    }

}
