package com.fengchao.equity.utils;

import lombok.Getter;

@Getter
public enum CardTicketStatusEnum {
    /**/
    CREATED(1, "未激活"),
    ACTIVE(2, "激活"),
    BOUND(3, "已绑定"),
    EXCHANGED(4, "已兑换"),
    OCCUPIED(5, "已占用"),
    USED(6, "已使用"),
    TIMEOUT(7, "已过期"),

    ;

    private int code;
    private String msg;

    CardTicketStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /*
    public static int String2Int(String status) {
        if (null != status && !status.isEmpty()) {
            int i;
            for (i = 0; i < CardTicketStatusEnum.values().length; i++) {
                if (CardTicketStatusEnum.values()[i].toString().equals(status)) {
                    return CardTicketStatusEnum.values()[i].getCode();
                }
            }
        }
        return 0;
    }
*/
    public static String Int2String(int code) {
        if (0 != code) {
            int i;
            for (i = 0; i < CardTicketStatusEnum.values().length; i++) {
                if (CardTicketStatusEnum.values()[i].getCode() == code) {
                    return CardTicketStatusEnum.values()[i].toString();
                }
            }
        }
        return "";
    }

}
