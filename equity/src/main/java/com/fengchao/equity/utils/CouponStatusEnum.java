package com.fengchao.equity.utils;

import lombok.Getter;

@Getter
public enum CouponStatusEnum {
    /*状态 （1：初始状态, 2：已发布， 3：未开始，4：进行中，5：已下线，6：待分配）*/
    INIT(1, "初始状态"),
    RELEASED(2, "已发布"),
    READY_GO(3, "未开始"),
    UNDERGOING(4, "进行中"),
    INVALID(5, "已下线"),
    READY_ASSIGN(6, "待分配"),
    ;

    private Integer code;
    private String msg;

    CouponStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean equals(Integer code){
        return (this.getCode().equals(code));
    }


}
