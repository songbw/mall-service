package com.fengchao.aggregation.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueryBean {
    private Integer offset;
    private Integer limit;
    private String appId;
    private String order;
}
