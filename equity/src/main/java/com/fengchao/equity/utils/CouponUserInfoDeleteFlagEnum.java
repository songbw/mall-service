package com.fengchao.equity.utils;

import lombok.Getter;

@Getter
public enum CouponUserInfoDeleteFlagEnum {
        /**/
    AVAILABLE(0, "未删除"),
    DELETED(1, "已删除"),

        ;

    private int code;
    private String msg;

    CouponUserInfoDeleteFlagEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
        }
}
