package com.fengchao.equity.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ThirdCouponParam {

    private String name;
    private String supplierMerchantId;
    private String supplierMerchantName;
    private Integer releaseTotal;
    private Integer perLimited;
    private String price;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date releaseStartDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date releaseEndDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date effectiveStartDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date effectiveEndDate;
    private String imageUrl;
    private String description;
    private String url;
}
