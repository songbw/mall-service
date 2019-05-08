package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarriageParam implements Serializable {
    private String merchantNo;
    private String amount;
}
