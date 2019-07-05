package com.fengchao.order.bean;

import lombok.Data;

@Data
public class DayStatisticsBean {
    private int orderPaymentAmount ;
    private int orderCount;
    private int orderPeopleNum;
    private int orderBackNum;
}
