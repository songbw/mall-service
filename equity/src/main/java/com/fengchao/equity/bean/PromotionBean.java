package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PromotionBean extends QueryBean {

    private Integer id;

    private String name;

    private Integer discountType;

    private Long promotionTypeId;

    private Integer status;

}

