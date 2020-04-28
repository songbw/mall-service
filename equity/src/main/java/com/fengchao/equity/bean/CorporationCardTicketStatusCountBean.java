package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 统计汇总： （特定企业购买的）各状态提货卡数量
 * @author clark
 * @since 2020/04/24
 * */
@Getter
@Setter
public class CorporationCardTicketStatusCountBean {
    private String corporationCode;
    List<CardTicketStatusCountBean> statusCount;
}
