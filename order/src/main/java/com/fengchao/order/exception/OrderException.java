package com.fengchao.order.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class OrderException extends RuntimeException {

    private String module = "Order";

    private Integer code = 500;

    private String msg = "Order Module Error!";

    public OrderException() {
        super();
    }

    public OrderException(Integer code, String msg) {
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
