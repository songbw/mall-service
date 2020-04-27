package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class PriceBean implements Serializable {
    private String skuId;
    private String price;
    private String sPrice;
    private int merchantId;
    private String spuId ;
}
