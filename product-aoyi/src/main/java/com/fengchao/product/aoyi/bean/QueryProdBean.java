package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.util.List;

@Data
public class QueryProdBean {
    private Integer offset;
    private Integer pageNo;
    private Integer pageSize;
    private List<String> couponMpus;
    private String excludeMpus;
    private String categories;
    private String brands;
}
