package com.fengchao.equity.bean;

import lombok.Data;

@Data
public class QueryBean {

    private Integer limit = 10;
    private Integer offset = 1;
    private String appId;
}
