package com.fengchao.order.rpc.extmodel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

    private Integer merchantId ;

    private Integer inventory ;

    private Integer brandId ;

    private String mpu;

    /**
     * 商品税率
     */
    private String taxRate;
}
