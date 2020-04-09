package com.fengchao.equity.utils;

import lombok.Getter;

@Getter
public enum CardInfoStatusEnum {
    /**/
    EDITING(1, "编辑"),
    RELEASED(2, "已发布"),
    INVALID(3, "已下架"),
    ;

    private Integer code;
    private String msg;

    CardInfoStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static boolean
    canEdit(Integer code){
        return (null != code && EDITING.getCode().equals(code));
    }

    public static boolean
    canBuy(Integer code){
        return (null != code && RELEASED.getCode().equals(code));
    }
}
