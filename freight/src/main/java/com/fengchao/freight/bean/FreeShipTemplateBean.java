package com.fengchao.freight.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class FreeShipTemplateBean {
    private Integer id;
    private String name;
    private Integer merchantId;
    private Boolean isDefault;
    private Integer mode;
    private Date createTime;
    private Date updateTime;
    private Integer status;
    private List<FreeShipRegionsBean> regions;
}
