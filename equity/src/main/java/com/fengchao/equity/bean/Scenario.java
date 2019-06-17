package com.fengchao.equity.bean;

import lombok.Data;

@Data
public class Scenario {
    private int type;
    private String[] couponSkus;
    private String[] excludeSkus;
    private String[] categories;
    private String[] brands;
}
