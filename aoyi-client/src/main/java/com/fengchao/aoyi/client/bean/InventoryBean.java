package com.fengchao.aoyi.client.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class InventoryBean implements Serializable {
    private String skuId;
    // 库存情况  我现在返回有设定：  00 有货   01不销售（下架）  02 无货   03 缺货（部分有货）
    private String state = "0";  // 0 无货 1 有货
    private String remainNum;
    private String price;
}
