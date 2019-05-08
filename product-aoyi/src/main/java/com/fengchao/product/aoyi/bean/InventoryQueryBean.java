package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InventoryQueryBean extends QueryBean implements Serializable{
    private String cityId ;
    private String countyId;
    private List<InventoryBean> skus ;
}
