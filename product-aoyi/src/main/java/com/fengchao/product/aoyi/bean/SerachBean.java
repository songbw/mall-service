package com.fengchao.product.aoyi.bean;

import lombok.Data;

@Data
public class SerachBean {
    private Integer limit;
    private Integer offset;
    private String query;
    private String categoryID;
    private String skuid;
    private String state;
    private String brand;
    private Integer merchantId;
    private Integer id;
}
