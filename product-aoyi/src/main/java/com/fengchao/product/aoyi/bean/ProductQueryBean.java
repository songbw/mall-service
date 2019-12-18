package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductQueryBean extends QueryBean implements Serializable{
    private String category ;
    private String brand ;
    private String keyword;
    private String skuProfix;
    private String priceOrder;
    private List<String> categories = new ArrayList<>();

}
