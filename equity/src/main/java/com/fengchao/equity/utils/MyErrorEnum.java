package com.fengchao.equity.utils;
/**
 * @author Clark
 * */
public enum MyErrorEnum {
    /* HTTP 信息*/
    RESPONSE_SUCCESS(200, "success"),
    RESPONSE_FUNCTION_ERROR(400, "failed"),

    /*一般性错误*/
    ACCOUNT_PASSWORD_WRONG(300001, "账号或密码错误"),

    PARAMETER_TIME_BLANK(300101, "时间缺失,格式：hh:mm:ss"),
    PARAMETER_TIME_WRONG(300102, "时间错误,格式：hh:mm:ss"),
    PARAMETER_DATE_WRONG(300103, "日期错误 格式：YYYY-MM-DD"),

    /* 业务类错误 */
    CARD_TICKET_BIND_EXCHANGE_FAILED(420100,"兑换失败,该提货券"),
    CARD_TICKET_STATUS_INACTIVE(420101,"兑换失败,提货券未激活"),
    CARD_TICKET_STATUS_BOUND(420103,"兑换失败,提货券已绑定"),
    CARD_TICKET_STATUS_EXCHANGED(420104,"兑换失败,提货券已兑换"),
    CARD_TICKET_STATUS_OCCUPIED(420105,"兑换失败,提货券已占用"),
    CARD_TICKET_STATUS_USED(420106,"兑换失败,提货券已使用"),
    CARD_TICKET_STATUS_TIMEOUT(420107,"兑换失败,提货券已过期"),
    CARD_TICKET_HEADER_APP_ID_BLANK(420108,"兑换失败,应用缺失渠道信息"),

    CARD_TICKET_MISSING(420201,"兑换失败,,未找到提货券信息"),
    CARD_INFO_APP_ID_NOT_MATCH(420202,"兑换失败,应用渠道与提货券渠道不符"),

    /* 内部异常错误*/
    COUPON_USE_INFO_INSERT_ERROR(500101,"兑换失败,增加记录失败"),

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


}
