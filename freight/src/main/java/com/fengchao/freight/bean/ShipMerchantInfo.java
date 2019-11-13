package com.fengchao.freight.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ShipMerchantInfo {
    private Integer merchantId;
    private List<MpuParam> mpuParams;
    private String merchantCode;
}
