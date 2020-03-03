package com.fengchao.equity.bean;

import lombok.Data;

import java.util.List;

@Data
public class QueryProdBean {
    private Integer offset;
    private Integer limit;
    private Integer pageNo;
    private Integer pageSize;
    private List<String> couponMpus;
    private String excludeMpus;
    private String categoryID;
    private String brands;
    private String appId;
}
