package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class StateBean implements Serializable {
    private String skuId;
    // 0：下架；1：上架
    private String state;
    private int merchantId;
}
