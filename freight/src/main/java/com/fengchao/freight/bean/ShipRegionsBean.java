package com.fengchao.freight.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShipRegionsBean {
    private Integer id;
    private Integer templateId;
    private Integer basePrice;
    private Integer baseAmount;
    private Integer cumulativePrice;
    private Integer cumulativeUnit;
    private String name;
    private String[] provinces;
    private Integer status;
}
