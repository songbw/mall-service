package com.fengchao.freight.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShipPriceBean {
    private Integer merchantId;
    private String merchantCode;
    private Float shipPrice;
}
