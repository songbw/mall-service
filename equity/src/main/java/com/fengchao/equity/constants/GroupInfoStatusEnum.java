package com.fengchao.equity.constants;

/**
 * 活动状态 1:新建 2：未开始(提交/发布) 3：进行中，4：已结束
 */
public enum  GroupInfoStatusEnum {

    CREATED(1, "新建"),
    UNSTART(2, "未开始(提交/发布)"),
    ONGOING(3, "进行中"),
    FINISH(4, "已结束");

    private Integer value;

    private String desc;

    GroupInfoStatusEnum(int value, String desc) {
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
