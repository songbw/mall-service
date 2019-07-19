package com.fengchao.product.aoyi.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class PromotionInfoBean implements Serializable {

    private Integer id;

    private String name;

    private String tag;

    private Integer promotionType;

    private Integer status;

    private Date startDate;

    private Date endDate;

    private Date createdDate;

    private String skuid;

    private String discount;
}
