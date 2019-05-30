package com.fengchao.aoyi.client.bean;

import lombok.Data;

@Data
public class SkusT {
    private String skuId;
    private String num;
    private String unitPrice;
    private String name = "";
    private String model = "";
    private String subOrderNo;
}
