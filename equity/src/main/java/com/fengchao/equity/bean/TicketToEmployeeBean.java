package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TicketToEmployeeBean {

    /**提货卡编码*/
    private String card;
    /**员工手机号*/
    private String phone;

    /**员工号(仅用于返回)*/
    private String employeeCode;

}
