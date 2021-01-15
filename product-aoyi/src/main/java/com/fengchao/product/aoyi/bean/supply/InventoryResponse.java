package com.fengchao.product.aoyi.bean.supply;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2021/1/15 16:18
 */
@Setter
@Getter
public class InventoryResponse extends SupplyBean{
    private String inventory ;
    private String status ;
}
