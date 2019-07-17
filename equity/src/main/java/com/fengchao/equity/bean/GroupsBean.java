package com.fengchao.equity.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GroupsBean extends QueryBean{
    private Integer id;
    private String name;
    private String skuid;
    private String imageUrl;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date effectiveStartDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date effectiveEndDate;
    private BigDecimal groupBuyingPrice;
    private String groupBuyingQuantity;
    private Integer groupMemberQuantity;
    private Integer limitedPerMember;
    private String paymentExpiration;
    private String description;
    private Integer campaignStatus;
    private Integer groupTotal;
}
