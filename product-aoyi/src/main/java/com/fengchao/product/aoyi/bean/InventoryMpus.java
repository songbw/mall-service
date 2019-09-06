package com.fengchao.product.aoyi.bean;

import lombok.Data;

@Data
public class InventoryMpus {
    private String mpu;
    // 0 无货 1 有货
    private String State = "0";
    //数量
    private int remainNum;
}
