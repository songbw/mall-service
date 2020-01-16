package com.fengchao.product.aoyi.starBean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2020/1/6 17:22
 */
@Setter
@Getter
public class CancelApplyRefundBean {
    private String serviceSn ;
    private String type ;
    private String orderSn ;
    private String reason ;
}
