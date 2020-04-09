package com.fengchao.equity.model;

import com.fengchao.equity.bean.CouponBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class CardTicketX {
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

    private String corporationCode;
    private String welfareOrderNo;
    private String cardInfoCode;

    private CardInfo cardInfo;

    private List<CouponBean> Coupons;

}
