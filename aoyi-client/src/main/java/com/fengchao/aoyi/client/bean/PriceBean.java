package com.fengchao.aoyi.client.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class PriceBean implements Serializable {
    private String skuId;
    private String price;
}
