package com.fengchao.guanaitong.bean;

import com.fengchao.guanaitong.constant.MyErrorEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author clark
 * */
@Getter
@Setter
public class ResultObject<T>{

    public static final String CODE = "code";
    public static final String MESSAGE = "msg";
    public static final String DATA = "data";
    public static final Integer MY_HTTP_ERROR_STATUS_CODE = 400;

    private Integer code;
    private String msg;
    private T data;

    public ResultObject(Integer code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public ResultObject(MyErrorEnum errorEnum, T data) {
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
        this.data = data;
    }

    public ResultObject(MyErrorEnum errorEnum) {
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
        this.data = null;
    }

    public ResultObject(T data) {
        this.code = MyErrorEnum.RESPONSE_SUCCESS.getCode();
        this.msg = MyErrorEnum.RESPONSE_SUCCESS.getMsg();
        this.data = data;
    }
}
