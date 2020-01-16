package com.fengchao.elasticsearch.domain;

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

    private Integer scheduleId;

    private Boolean dailySchedule;

    private String promotionImage;

    private Integer perLimited;
}
