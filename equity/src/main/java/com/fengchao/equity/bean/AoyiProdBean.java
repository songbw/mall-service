package com.fengchao.equity.bean;

import lombok.Data;

@Data
public class AoyiProdBean {
    private Integer id;
    private String skuid;
    private String mpu;
    private String brand;
    private String category;
    private String image;
    private String model;
    private String name;
    private String weight;
    private String upc;
    private String saleunit;
    // 上下架状态 1：已上架；0：已下架
    private String state;
    private String price;
    private String sprice;
    private String imagesUrl;
    private String introductionUrl;
    private String categoryName;
    private String introduction;
    private String prodParams;
}
