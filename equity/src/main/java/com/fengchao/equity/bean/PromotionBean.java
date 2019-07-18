package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class PromotionBean {

    private Integer id;

    private String name;

    private String tag;

    private Integer promotionType;

    private Integer status;

    private Date startDate;

    private Date endDate;

    private Date createdDate;

    private Integer limit;
    private Integer offset;
}

