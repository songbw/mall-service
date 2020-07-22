package com.fengchao.guanaitong.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误定义
 * @author clark
 * @since 2020-05-14
 */
@Getter
public enum MyErrorEnum {
    /*错误码*/
    RESPONSE_SUCCESS(200, "success"),
    RESPONSE_FUNCTION_ERROR(400, "failed"),

    COMMON_PARAMETER_ERROR(400000, "一般参数错误"),
    PARAM_URL_BLANK(400002,"缺少输入参数 url"),
    HTTP_HEADER_APPID_BLANK(400003,"缺少Header:appId"),

    CONFIG_BLANK_FOR_IAPPID(400101, "找不到参数appId对应的关爱通配置"),
    REQUEST_PATH_BLANK(400102, "访问路径缺失"),

    GAT_HTTP_ERROR(420000, "访问关爱通异常"),
    ;

    private Integer code;
    private String msg;

    MyErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String buildCodeMsg(Integer code, String msg){
        return "@"+code.toString()+"@"+msg;
    }

    public String buildCodeMsg(){
        return "@"+this.code.toString()+"@"+this.msg;
    }

    public String buildCodeMsg(String msg){
        return "@"+this.code.toString()+"@"+this.msg+msg;
    }

    public String getCodeMsg(){
        return "@"+this.code.toString()+"@"+this.msg;
    }

    public static Map<String,Integer> getMap(){
        Map<String,Integer> map = new HashMap<>();
        for (MyErrorEnum item : MyErrorEnum.values()) {
            map.put(item.getMsg(),item.getCode());
        }
        return map;
    }
}
