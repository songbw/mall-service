package com.fengchao.elasticsearch.domain;

import lombok.Data;

@Data
public class Product {
    private Integer id;

//    @JestId
    private String skuid;

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
}
