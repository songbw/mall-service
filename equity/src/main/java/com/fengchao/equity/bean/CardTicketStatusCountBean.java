package com.fengchao.equity.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 统计项： （特定企业购买的）特定状态提货卡数量
 * @author clark
 * @since 2020/04/24
 * */

@Getter
@Setter
public class CardTicketStatusCountBean {
    private Integer status;
    private Integer count;
}
