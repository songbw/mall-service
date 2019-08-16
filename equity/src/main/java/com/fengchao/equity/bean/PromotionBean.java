package com.fengchao.equity.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PromotionBean extends QueryBean {

    private Integer id;

    private String name;

    /**
     * 优惠方式（0：减价， 1：折扣）
     */
    private Integer discountType;

    /**
     * 活动类型 1:秒杀 2:优选 3:普通
     */
    private String typeName;

    private Long promotionTypeId;

    private Integer status;

    private String dailySchedule;

}

