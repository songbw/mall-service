package com.fengchao.equity.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CouponSearchBean {
    private String name;
    private Integer releaseTotal;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date releaseStartDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date releaseEndDate;
    private Integer status;
    private String couponType;
    private String scenarioType;
    private Integer limit;
    private Integer offset;
}
