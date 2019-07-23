package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PromotionBean extends QueryBean {

    private String name;

    private Integer promotionType;

    private Integer status;

}

