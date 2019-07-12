package com.fengchao.equity.constants;

/**
 * 拼购team状态 1：新建 2：进行中(组团中) 3:满员中 4：组团成功，5：组团失效/失败
 */
public enum GroupTeamStatusEnum {

    //
    CREATED(1, "新建"),
    ONGOING(2, "进行中(组团中)"),
    FULLMEMBER(3, "满员中"),
    SUCCESS(4, "组团成功"),
    FAIL(5, "组团失效/失败");

    private Integer value;

    private String desc;

    GroupTeamStatusEnum(Integer value, String desc) {
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
