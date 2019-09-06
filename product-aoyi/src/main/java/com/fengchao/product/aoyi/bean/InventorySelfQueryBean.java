package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InventorySelfQueryBean implements Serializable{
    private List<InventoryMpus> inventories;
}
