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
    private Integer merchantId;
    private List<String> categories = new ArrayList<>();
    private String skuPrefix;
    private List<Integer> merchantIds = new ArrayList<>() ;
    private List<String> merchantCodes = new ArrayList<>() ;
    private String state;
    private Integer id ;
    private String name ;
    private String categoryId ;
    private String skuid ;
    private String mpu ;
    private String order ;
    private String minPrice ;
    private String maxPrice ;
    private String mpuPrefix ;
    private Integer merchantHeader;

}
