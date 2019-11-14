package com.fengchao.sso.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author songbw
 * @date 2019/11/14 11:43
 */
@Setter
@Getter
public class BalanceSumBean {
    private Integer id;
    private String telephone;
    private Integer amount;
    private String openId;
    private Integer initAmount = 0;
    private Integer chargeAmount = 0;
    private Integer paymentAmount = 0;
    private Integer refundAmount = 0;
    private Date createdAt ;
    private String operator;
}
