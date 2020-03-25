package com.fengchao.equity.utils;

import lombok.Getter;

@Getter
public enum CouponCollectTypeEnum {

    /*领取类型（1: 主动领取, 2: 主动发放, 3: 积分兑换）*/
    AUTO_GET(1, "主动领取"),
    AUTO_ASSIGN(2, "主动发放"),
    EXCHANGE(3, "积分兑换"),
    MANUAL_ASSIGN(4, "人工分配"),
    ;

    private Integer code;
    private String msg;

    CouponCollectTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean equals(Integer code){
        return (this.getCode().equals(code));
    }
}
