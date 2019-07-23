package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Scenario implements Serializable {
    private int type;
    private String[] couponMpus;
    private String[] excludeMpus;
    private String[] categories;
    private String[] brands;
}
