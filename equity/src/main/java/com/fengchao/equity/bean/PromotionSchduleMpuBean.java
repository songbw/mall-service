package com.fengchao.equity.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fengchao.equity.model.PromotionScheduleX;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class PromotionSchduleMpuBean {
    private Integer id;

    private String name;

    private String tag;

    private Integer status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endDate;

    private Date createdDate;

    private Integer discountType;

    private Long promotionTypeId;

    private Boolean dailySchedule;

    private PromotionScheduleX schedule;

}
