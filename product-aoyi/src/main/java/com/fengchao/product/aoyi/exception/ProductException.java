package com.fengchao.product.aoyi.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductException extends RuntimeException {

    private String module = "Product";

    private Integer code = 500;

    private String message = "Product Module Error!";

    public ProductException() {
        super();
    }

    public ProductException(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

}
