package com.fengchao.freight.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Clark
 * */
@Getter
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
    TEMPLATE_EXISTED(420100, "该供货商模板已经存在"),


    /* 内部异常错误*/
    DB_INSERT_ERROR(500101, "增加记录失败"),

    ;

    private Integer code;
    private String msg;

    MyErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String buildCodeMsg(Integer code, String msg){
        //log.error(msg);
        return "@"+code.toString()+"@"+msg;
    }

    public String buildCodeMsg(){
        //log.error(this.msg);
        return "@"+this.code.toString()+"@"+this.msg;
    }

    public String getCodeMsg(){
        return "@"+this.code.toString()+"@"+this.msg;
    }

    public static Map<String,String> getMap(){
        Map<String,String> map = new HashMap<>();
        for (MyErrorEnum item : MyErrorEnum.values()) {
            map.put(item.getCode().toString(),item.getMsg());
        }
        return map;
    }
}
