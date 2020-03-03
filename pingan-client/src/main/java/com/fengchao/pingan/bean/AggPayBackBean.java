package com.fengchao.pingan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2019/12/4 16:02
 */
@Getter
@Setter
public class AggPayBackBean {
    private String orderNo ;
    private String refundFee ;
    private String payFee ;
    private String refundNo ;
    private String payType = "pingan" ;
    private String tradeDate ;
    private String tradeNo ;
}
