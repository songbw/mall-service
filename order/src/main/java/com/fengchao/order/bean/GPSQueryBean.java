package com.fengchao.order.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class GPSQueryBean extends QueryBean implements Serializable{
    private String latitude ;
    private String longitude ;
    private String locTime;
    private String country;
    private String province;
    private String city;
    private String county;
}
