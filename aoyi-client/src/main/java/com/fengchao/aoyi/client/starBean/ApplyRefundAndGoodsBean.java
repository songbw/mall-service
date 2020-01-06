package com.fengchao.aoyi.client.starBean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2020/1/6 15:54
 */
@Setter
@Getter
public class ApplyRefundAndGoodsBean {
    private String orderSn ;
    private String reason ;
    private String code ;
    private String returnType ;
    private String serviceSn ;
    private String type ;
}
