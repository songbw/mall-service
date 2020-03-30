package com.fengchao.equity.utils;

import lombok.Getter;

@Getter
public enum CouponTypeEnum {
    /*优惠券类型（0:满减券 1:代金券 2:折扣券 3:服务券 4:礼包券）*/
    CONDITION_DISCOUNT(0, "满减券"),
    AS_MONEY(1, "代金券"),
    DISCOUNT(2, "折扣券"),
    SERVICE(3, "积分兑换"),
    GIFT_PACKAGE(4, "礼包券"),
    ;

    private Integer code;
    private String msg;

    CouponTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean equals(Integer code){
        return (this.getCode().equals(code));
    }

}
