package com.fengchao.freight.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CarriageBean {
    private String provinceId;
    private List<ShipMerchantInfo> merchantInfos;
    private Float totalPrice;
}
