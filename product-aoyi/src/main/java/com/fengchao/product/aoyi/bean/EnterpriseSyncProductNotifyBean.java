package com.fengchao.product.aoyi.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author songbw
 * @date 2020/12/30 15:34
 */
@Setter
@Getter
public class EnterpriseSyncProductNotifyBean {
    private String appId ;
    private List<String> spus ;
    private String sign ;
}
