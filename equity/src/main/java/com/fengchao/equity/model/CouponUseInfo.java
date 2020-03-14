package com.fengchao.equity.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CouponUseInfo {
    private Integer id;

    private Integer couponId;

    private String userCouponCode;

    private String userOpenId;

    private String code;

    private Date collectedTime;

    private Date consumedTime;

    private String orderId;

    private Integer status;

    private String url;

    private Integer type;

    private Date effectiveStartDate;

    private Date effectiveEndDate;

    private String appId;

    private Integer deleteFlag;


}
