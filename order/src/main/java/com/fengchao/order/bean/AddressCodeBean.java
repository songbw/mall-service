package com.fengchao.order.bean;

import lombok.Data;

@Data
public class AddressCodeBean {
    private String provinceId;
    private String cityId;
    private String countyId;
    private String zipCode;
}
