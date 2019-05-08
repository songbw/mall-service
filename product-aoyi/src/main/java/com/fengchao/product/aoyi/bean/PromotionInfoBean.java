package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.util.Date;

@Data
public class PromotionInfoBean {

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
