package com.fengchao.aoyi.client.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author song
 * @2020-02-09 12:08 下午
 **/
@Setter
@Getter
public class StarBackBean {
    private String appKey ;
    private long currentTime ;
    private String sign ;
    private String outOrderNo ;
    private String orderSn ;
    private String oldStatus ;
    private String oldStatusName ;
    private String newStatus ;
    private String newStatusName ;
    private String statusUpdateTime ;
    private String updateType ;
    private String serviceSn ;
}
