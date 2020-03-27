package com.fengchao.equity.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class CardInfoX {
    private Integer id;

    private String name;

    private String amount;

    private Short type;

    private Integer effectiveDays;

    private Date createTime;

    private Date updateTime;

    private Short status;

    private Short isDelete;

    private String appId;
    private String corporationCode;
    private String code;

    private List<CardAndCoupon> couponIds;

    private List<CardTicket> tickets;

}
