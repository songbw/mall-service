package com.fengchao.equity.utils;

public enum MyErrorEnum {
    /**/
    RESPONSE_SUCCESS(200, "success"),
    RESPONSE_FUNCTION_ERROR(400, "failed"),

    COMMON_PARAMETER_ERROR(400000, "一般参数错误"),
    PARAMETER_TIME_BLANK(400001, "时间缺失,格式：hh:mm:ss"),
    PARAMETER_TIME_WRONG(400002, "时间错误,格式：hh:mm:ss"),
    PARAMETER_DATE_WRONG(400003, "日期错误 格式：YYYY-MM-DD"),


    UTILS_MD5_BUILD_ERROR(490001, "MD5构造错误"),
    ;

    private Integer code;
    private String msg;

    MyErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCodeMsg(Integer code, String msg) {
        return "@" + code.toString() + "@" + msg;
    }

    public String getCodeMsg() {
        return "@" + this.code.toString() + "@" + this.msg;
    }
}
