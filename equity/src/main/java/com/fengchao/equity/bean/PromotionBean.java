package com.fengchao.equity.bean;

import lombok.Data;

@Data
public class PromotionBean {

    private String name;
    private Integer promotionType;
    private Integer status;
    private Integer limit;
    private Integer offset;
}

