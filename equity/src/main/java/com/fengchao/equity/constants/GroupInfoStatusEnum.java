package com.fengchao.equity.constants;

/**
 * 活动状态（1：未开始，2：进行中，3：已结束）
 */
public enum  GroupInfoStatusEnum {

    UNSTART(1), ONGOING(2), FINISH(3);

    private Integer value;

    GroupInfoStatusEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
