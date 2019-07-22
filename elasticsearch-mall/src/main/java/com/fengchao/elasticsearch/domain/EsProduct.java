package com.fengchao.elasticsearch.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * 搜索中的商品信息
 * Created by song on 2019/5/16.
 */
@Data
@Document(indexName = "product", shards = 1,replicas = 0)
public class EsProduct implements Serializable {
    @Id
    private Long id;
    @Field(type = FieldType.Keyword)
    private String skuid;
    private Long brandId;
    private String categoryId;
    @Field(type = FieldType.Keyword)
    private String brand;
    @Field(type = FieldType.Keyword)
    private String category;

    private String image;

    private String model;
    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String name;
    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String keywords;
    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String subTitle;

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

    private String imageExtend;

    private String imagesUrlExtend;

    private String introductionUrlExtend;

    private Integer promotionType;

    private Integer sort;
}
