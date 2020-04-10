package com.fengchao.equity.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum CouponUseStatusEnum {
    /**/
    AVAILABLE(1, "未使用"),
    OCCUPIED(2, "已占用"),
    USED(3, "已使用"),
    INVALID(4, "已失效"),
    ;

    private int code;
    private String msg;

    CouponUseStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static List<Integer> canRefundStatusList(){
        List<Integer> list = new ArrayList<>();
        list.add(AVAILABLE.getCode());
        list.add(INVALID.getCode());

        return list;
    }

    public static boolean canRefund(Integer code){
        return (AVAILABLE.getCode() == code || INVALID.getCode() == code);
    }

    public static int String2Int(String status) {
        if (null != status && !status.isEmpty()) {
            int i;
            for (i = 0; i < CouponUseStatusEnum.values().length; i++) {
                if (CouponUseStatusEnum.values()[i].toString().equals(status)) {
                    return CouponUseStatusEnum.values()[i].getCode();
                }
            }
        }
        return 0;
    }

    public static String Int2String(int code) {
        if (0 != code) {
            int i;
            for (i = 0; i < CouponUseStatusEnum.values().length; i++) {
                if (CouponUseStatusEnum.values()[i].getCode() == code) {
                    return CouponUseStatusEnum.values()[i].toString();
                }
            }
        }
        return "";
    }
    public static String int2msg(int code){
        if (0 != code) {
            int i;
            for (i = 0; i < CouponUseStatusEnum.values().length; i++) {
                if (CouponUseStatusEnum.values()[i].getCode() == code) {
                    return CouponUseStatusEnum.values()[i].getMsg();
                }
            }
        }
        return "";
    }

}
