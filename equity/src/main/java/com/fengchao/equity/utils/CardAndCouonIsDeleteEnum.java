package com.fengchao.equity.utils;

import lombok.Getter;

@Getter
public enum CardAndCouonIsDeleteEnum {
/**/
    VALID(1, "有效"),
    INVALID(2, "无效"),

    ;

    private int code;
    private String msg;

    CardAndCouonIsDeleteEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static int String2Int(String status) {
        if (null != status && !status.isEmpty()) {
            int i;
            for (i = 0; i < CardAndCouonIsDeleteEnum.values().length; i++) {
                if (CardAndCouonIsDeleteEnum.values()[i].toString().equals(status)) {
                    return CardAndCouonIsDeleteEnum.values()[i].getCode();
                }
            }
        }
        return 0;
    }

    public static String Int2String(int code) {
        if (0 != code) {
            int i;
            for (i = 0; i < CardAndCouonIsDeleteEnum.values().length; i++) {
                if (CardAndCouonIsDeleteEnum.values()[i].getCode() == code) {
                    return CardAndCouonIsDeleteEnum.values()[i].toString();
                }
            }
        }
        return "";
    }
    public static String int2msg(int code){
        if (0 != code) {
            int i;
            for (i = 0; i < CardAndCouonIsDeleteEnum.values().length; i++) {
                if (CardAndCouonIsDeleteEnum.values()[i].getCode() == code) {
                    return CardAndCouonIsDeleteEnum.values()[i].getMsg();
                }
            }
        }
        return "";
    }
}
