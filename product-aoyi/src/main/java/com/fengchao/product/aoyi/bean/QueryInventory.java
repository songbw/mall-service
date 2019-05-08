package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QueryInventory {
    private String cityId;
    private String countyId;
    private List<InventorySkus> skuIds = new ArrayList<>();
}
