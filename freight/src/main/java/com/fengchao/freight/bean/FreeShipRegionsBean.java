package com.fengchao.freight.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeShipRegionsBean {
    private Integer id;
    private Integer templateId;
    private Integer fullAmount;
    private String name;
    private String[] provinces;
    private Integer status;
}
