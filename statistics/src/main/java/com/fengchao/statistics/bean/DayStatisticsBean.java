package com.fengchao.statistics.bean;

import lombok.Data;

@Data
public class DayStatisticsBean {
    private float orderPaymentAmount ;
    private int orderCount;
    private int orderPeopleNum;
    private int orderBackNum;
}
