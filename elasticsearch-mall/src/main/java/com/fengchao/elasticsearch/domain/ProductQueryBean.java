package com.fengchao.elasticsearch.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductQueryBean extends QueryBean implements Serializable{
    private String category ;
    private String brand ;
    private String keyword;
}
