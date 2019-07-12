package com.fengchao.equity.constants;

/**
 * 拼购成员(member)状态 1:预备 2：正式 3：失效 4:退款中
 */
public enum GroupMemberStatusEnum {

    CANDIDATE(1, "预备"),
    REGULAR(2, "正式"),
    INVALID(3, "失效"),
    REFUNDING(4, "退款中");

    private Integer value;

    private String desc;

    GroupMemberStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
