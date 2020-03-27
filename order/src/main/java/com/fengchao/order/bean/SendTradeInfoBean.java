package com.fengchao.order.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/12/24 15:13
 */
@Setter
@Getter
public class SendTradeInfoBean {
    private String outer_trade_no ;
    private TradeInfoBean trade_info ;
}
