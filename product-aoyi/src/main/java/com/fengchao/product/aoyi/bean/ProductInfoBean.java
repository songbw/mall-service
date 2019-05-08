package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductInfoBean {

    private Integer id;

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

    private String categoryName;

    private String imageExtend;

    private String imagesUrlExtend;

    private String introductionUrlExtend;

    private List<PromotionInfoBean> promotion = new ArrayList<>();
}
