package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductQueryBean extends QueryBean implements Serializable{
    private String category ;
    private String brand ;
    private String keyword;
    private String skuProfix;
    private String priceOrder;
}
