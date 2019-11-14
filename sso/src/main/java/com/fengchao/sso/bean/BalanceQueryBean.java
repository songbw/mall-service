package com.fengchao.sso.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 余额查询类
 */
@Setter
@Getter
public class BalanceQueryBean extends QueryBean {
    private String telephone ;
    private String start;
    private String end ;
    private Integer Type ;
    private Integer balanceId;
}
