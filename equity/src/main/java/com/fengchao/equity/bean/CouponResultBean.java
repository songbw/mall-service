package com.fengchao.equity.bean;

import lombok.Data;

import java.util.Date;

@Data
public class CouponResultBean {

    private Integer id;
    private String name;
    private Integer releaseTotal;
    private Integer releaseNum;
    private Date releaseStartDate;
    private Date releaseEndDate;
    private Integer status;
}
