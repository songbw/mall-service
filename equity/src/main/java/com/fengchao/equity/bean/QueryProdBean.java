package com.fengchao.equity.bean;

import lombok.Data;

import java.util.List;

@Data
public class QueryProdBean {
    private Integer offset;
    private Integer pageNo;
    private Integer pageSize;
    private List<String> couponSkus;
    private String excludeSkus;
    private String categories;
    private String brands;
}
