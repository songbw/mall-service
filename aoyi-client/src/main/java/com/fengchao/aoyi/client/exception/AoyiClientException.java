package com.fengchao.aoyi.client.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class AoyiClientException extends RuntimeException {

    private String module = "Aoyi Client";

    private Integer code = 500;

    private String msg = "Aoyi Client Error!";

    public AoyiClientException() {
        super();
    }

    public AoyiClientException(Integer code, String msg) {
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
