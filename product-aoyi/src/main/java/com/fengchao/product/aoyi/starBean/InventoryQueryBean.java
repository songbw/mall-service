package com.fengchao.product.aoyi.starBean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2020/1/2 15:15
 */
@Setter
@Getter
public class InventoryQueryBean {
    // 区域ID 省市区唯一ID, 使用英文逗号拼接
    private String areaId ;
    // (弃用)最多查200个,skuId 多个以英文逗号拼接
    private String skuIds ;
    //  商品ID，最多查200个,code 多个以英文逗号拼接
    private String codes ;
}
