package com.fengchao.equity.constants;

/**
 * lts 相关业务常量定义
 */
public class LTSConstants {

    /**
     * lts 针对equity的nodeName
     */
    public static final String LTS_EQUITY_NODE_NAME = "equity_trade_TaskTracker";

    /**
     * 拼购活动开始触发 用于lts的taskid的前缀
     */
    public static final String GROUP_START_TRIGGER_PREFIX = "group_start_trigger_";

    /**
     * 拼购活动结束触发 用于lts的taskid的前缀
     */
    public static final String GROUP_END_TRIGGER_PREFIX = "group_end_trigger";

    /**
     * 拼购lts任务类型 开始类型
     */
    public static final String GROUP_TYPE_START_TASK = "group_type_start_task";

    /**
     * 拼购lts任务类型 结束类型
     */
    public static final String GROUP_TYPE_END_TASK = "group_type_start_task";
}
