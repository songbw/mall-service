package com.fengchao.statistics.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QueryUrlBean implements Serializable {

    private Integer pageIndex;
    private Integer pageSize;

}
