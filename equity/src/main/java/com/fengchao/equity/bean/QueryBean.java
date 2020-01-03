package com.fengchao.equity.bean;

import lombok.Data;

import java.util.List;

@Data
public class QueryBean {
    private Integer limit = 10;
    private Integer offset = 1;
    private String appId;
    private List<Integer> ids;
}
