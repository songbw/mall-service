package com.fengchao.order.rpc.extmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class ShipTemplateBean {

    private Integer id;
    private Integer shipMpuId;
    private String name;
    private Integer merchantId;
    private Boolean isDefault;
    private Integer mode;
    private Date createTime;
    private Date updateTime;
    private Integer status;
    private List<ShipRegionsBean> regions;
}
