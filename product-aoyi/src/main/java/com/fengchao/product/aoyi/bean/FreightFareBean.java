package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class FreightFareBean implements Serializable {
    private String freightFare;
    private String merchantNo; // 商户号
    private String orderNo;
}
