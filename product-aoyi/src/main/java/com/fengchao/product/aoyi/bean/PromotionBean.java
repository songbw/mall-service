package com.fengchao.product.aoyi.bean;

import lombok.Data;

@Data
public class PromotionBean {

    private String name;
    private Integer promotionType;
    private Integer status;
    private Integer limit;
    private Integer offset;
}

