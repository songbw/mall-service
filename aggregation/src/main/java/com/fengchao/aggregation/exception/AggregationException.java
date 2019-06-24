package com.fengchao.aggregation.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class AggregationException extends RuntimeException {

    private String module = "Aggregation";

    private Integer code = 500;

    private String msg = "Aggregation Error!";

    public AggregationException() {
        super();
    }

    public AggregationException(Integer code, String msg) {
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
