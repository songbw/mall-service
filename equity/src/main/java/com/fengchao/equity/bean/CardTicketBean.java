package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardTicketBean {

    private Integer pageNo = 1;
    private Integer pageSize = 10;

    private int cardId;
    private int num = 1;
    private String remark;
    private String card;
    private String password;
    private String openId;
    private Integer couponId;
    private Integer id;
    private String appId;
    private String corporationCode;
    private String welfareOrderNo;
    private String cardInfoCode;
    private String employeeCode;

}
