package com.fengchao.product.aoyi.bean.supply;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author songbw
 * @date 2021/1/15 15:57
 */
@Setter
@Getter
public class SupplyInventoryBean {
    private String areaId ;
    private List<SupplyBean> list ;
}
