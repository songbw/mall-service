package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基于 CardTicket 去掉了password, 增加了orderIdList
 * */

@Getter
@Setter
public class CardTicketIncludeOrderIdBean {

    private Integer id;

    private Integer cardId;

    private String card;

    private String userCouponCode;

    private String openId;

    private Date createTime;

    private Date updateTime;

    private Date activateTime;

    private Date consumedTime;

    private Date endTime;

    private Short status;

    private String remark;

    private Short isDelete;

    private String corporationCode;

    private String welfareOrderNo;

    private String cardInfoCode;

    private String employeeCode;

    private Date assignTime;

    ///CardTicket之外从CouponUseInfo中获取
    private String orderIdList;
}
