package com.fengchao.product.aoyi.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ProductException extends RuntimeException {

    private String module = "Product";

    private Integer code = 500;

    private String msg = "Product Module Error!";

    public ProductException() {
        super();
    }

    public ProductException(Integer code, String msg) {
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
