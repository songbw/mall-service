package com.fengchao.aggregation.bean;

import lombok.Data;

import java.util.Date;

@Data
public class AggregationBean {
    private Integer id;
    private String name;
    private Date effectivedate;
    private String backgroundcolor;
    private String homePage;
    private Integer status;
    private Object content;
    private Integer offset;
    private Integer limit;
    private Integer groupId;
    private String order;
    private Integer merchantId;
}
