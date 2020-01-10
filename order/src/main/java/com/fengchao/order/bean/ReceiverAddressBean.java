package com.fengchao.order.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songbw
 * @date 2020/1/8 16:34
 */
@Setter
@Getter
public class ReceiverAddressBean {
    private Integer orderDetailId ;
    private String receiverName;
    private String mobile;
    private String provinceId;
    private String provinceName;
    private String cityId;
    private String cityName;
    private String countyId;
    private String countyName;
    private String address;
    private String zip;
}
