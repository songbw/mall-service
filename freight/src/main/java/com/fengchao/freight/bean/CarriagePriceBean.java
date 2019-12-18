package com.fengchao.freight.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CarriagePriceBean {
    private List<ShipPriceBean> priceBeans;
    private Float totalPrice;

}
