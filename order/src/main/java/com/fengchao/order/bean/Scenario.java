package com.fengchao.order.bean;

import lombok.Data;

@Data
public class Scenario {
    private int type;
    private String[] couponMpus;
    private String[] excludeMpus;
    private String[] categories;
    private String[] brands;
}
