package com.fengchao.equity.bean;

import lombok.Data;

@Data
public class TouShiCoupon {

    private String code;
    private String price;
    private String name;
    private String effectiveStartDate;
    private String effectiveEndDate;
    private String description;
    private String url;

}
