package com.fengchao.product.aoyi.bean;

import com.fengchao.product.aoyi.model.StarProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author song
 * @2020/02/04 10:32 上午
 **/
@Getter
@Setter
public class StarSkuBean {
    private Integer id;

    private String code;

    private String goodsLogo;

    private Integer purchaseQty;

    private String skuId;

    private Integer status;

    private String spuId;

    private String price;

    private List<StarProperty> propertyList ;
}
