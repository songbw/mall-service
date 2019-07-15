package com.fengchao.statistics.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryUrlStatisBean implements Serializable {

    private Integer pageIndex;
    private Integer pageSize;
    private String dayStart;
    private String dayEnd;
    private String urlKey;

}
