package com.fengchao.order.constants;

import lombok.extern.slf4j.Slf4j;

/**
 * 各自APP平台的枚举值
 */
@Slf4j
public enum AppPlatformEnum {

    UNKNOWN("-1", "UNKNOWN", "未知"),
    GAT_ZIYING("09", "GAT_ZIYING", "关爱通自营"),
    GAT_SUNING("10", "GAT_SUNING", "关爱通苏宁"),
    WUXI("11", "WUXI", "无锡市民卡"),
    FNEGCHAO_ZIYING_PULIC("01", "FNEGCHAO_ZIYING_PULIC", "凤巢自营公众号"),
    ZUIZHUHAI("12", "ZUIZHUHAI", "最珠海"),
    WEIXIN_MINI("02", "WEIXIN_MINI", "微信小程序");

    private String code;

    private String value;

    private String desc;

    AppPlatformEnum(String code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     *
     * @param code
     * @return
     */
    public static AppPlatformEnum getAppTerminalTypeEnum(String code) {
        switch (code) {
            case "09":
                return GAT_ZIYING;
            case "10":
                return GAT_SUNING;
            case "11":
                return WUXI;
            case "01":
                return FNEGCHAO_ZIYING_PULIC;
            case "12":
                return ZUIZHUHAI;
            case "02":
                return WEIXIN_MINI;

            default:
                log.warn("未获取到有效的AppTerminalTypeEnum枚举值 code:{}", code);
                return UNKNOWN;
        }
    }
}
