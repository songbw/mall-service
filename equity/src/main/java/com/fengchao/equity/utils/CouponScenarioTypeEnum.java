package com.fengchao.equity.utils;

import lombok.Getter;

@Getter
public enum CouponScenarioTypeEnum {
    /*使用场景类型（1：SKU，2：全场，3：类目品牌卷）*/
    SKU(1, "SKU"),
    ALL(2, "全场"),
    SPECIAL(3, "类目品牌卷"),
    
    ;

    private Integer code;
    private String msg;

    CouponScenarioTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean equals(Integer code){
        return (this.getCode().equals(code));
    }

}
