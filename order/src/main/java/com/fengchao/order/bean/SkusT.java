package com.fengchao.order.bean;

import lombok.Data;

@Data
public class SkusT {
    private String skuId;
    private String num;
    private String unitPrice;
    private String salePrice;
    private Integer promotionId;
    private String name = "";
    private String model = "";
    private String subOrderNo;
}
