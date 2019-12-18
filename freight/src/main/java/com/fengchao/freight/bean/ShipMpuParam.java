package com.fengchao.freight.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ShipMpuParam {
    private Integer merchantId;
    private String provinceId;
    private List<MpuParam> mpuParams;
    private Float totalPrice;
    private String mpu;
}
