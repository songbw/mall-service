package com.fengchao.equity.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class EquityException extends RuntimeException {

    private String module = "Equity";

    private Integer code = 500;

    private String msg = "Equity Error!";

    public EquityException() {
        super();
    }

    public EquityException(Integer code, String msg) {
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
