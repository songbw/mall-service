package com.fengchao.order.constants;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author tom
 * @Date 19-9-24 下午8:19
 */
@Slf4j
public enum IStatusEnum {

    VALID(1, "valid", "有效"),
    INVALID(2, "invalid", "无效");

    private Integer code;

    private String value;

    private String desc;

    IStatusEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return this.desc;
    }

    public static IStatusEnum getIStatusEnum(Integer code) {
        switch (code) {
            case 1:
                return VALID;
            case 2:
                return INVALID;

            default:
                log.warn("获取IStatusEnum枚举出错 code:{}", code);
                throw new RuntimeException("获取IStatusEnum枚举出错");
        }
    }
}
