package com.fengchao.pingan.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class PinganClientException extends RuntimeException {

    private String module = "Pingan Client";

    private Integer code = 500;

    private String msg = "Pingan Client Error!";

    public PinganClientException() {
        super();
    }

    public PinganClientException(Integer code, String msg) {
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
