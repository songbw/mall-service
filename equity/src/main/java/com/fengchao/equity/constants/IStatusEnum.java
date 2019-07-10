package com.fengchao.equity.constants;

public enum IStatusEnum {

    VALID(1), INVALID(2);

    private Integer value;

    IStatusEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
