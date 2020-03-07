package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CardDetailsBean {

    //本字段是本类与CardTicket的唯一区别.来自CouponUseInfo
    private String orderId;

    private Integer id;

    private Integer cardId;

    private String card;

    private String password;

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
}
