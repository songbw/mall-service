package com.fengchao.order.rpc.extmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CouponBean {

    private Integer id;
    private String name;
    private String supplierMerchantId;
    private String supplierMerchantName;
    private Integer releaseTotal;
    private Integer releaseNum;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date releaseStartDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date releaseEndDate;
    private Integer status;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date effectiveStartDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date effectiveEndDate;
    private String description;
    private List<Object> excludeDates;
    private String url;
    private String category;
    private int[] tags;
    private String imageUrl;
    private Date createDate;
    private Integer userCollectNum;
}
