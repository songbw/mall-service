package com.fengchao.equity.bean;

import lombok.Data;

@Data
public class PromotionBean extends QueryBean{

    private String name;
    private Integer promotionType;
    private Integer status;
}

