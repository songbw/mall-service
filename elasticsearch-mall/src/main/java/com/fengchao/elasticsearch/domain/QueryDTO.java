package com.fengchao.elasticsearch.domain;

import lombok.Data;

@Data
//@Builder
public class QueryDTO {
    private String origin;
    private String category;
    private String name;
    private String brand;
    private Float minScore;
    private String orderBy;
}
