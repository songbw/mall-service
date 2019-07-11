package com.fengchao.equity.constants;

/**
 * 活动状态（1：未开始，2：进行中，3：已结束）
 */
public enum  GroupInfoStatusEnum {

    CREATED(1), UNSTART(2), ONGOING(3), FINISH(4);

    private Integer value;

    GroupInfoStatusEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
