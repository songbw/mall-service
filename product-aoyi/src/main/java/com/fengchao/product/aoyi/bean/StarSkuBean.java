package com.fengchao.product.aoyi.bean;

import com.fengchao.product.aoyi.model.AppSkuPrice;
import com.fengchao.product.aoyi.model.StarProperty;
import com.fengchao.product.aoyi.model.StarSku;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author song
 * @2020/02/04 10:32 上午
 **/
@Getter
@Setter
public class StarSkuBean extends StarSku {

    private List<StarProperty> propertyList ;

    private List<AppSkuPrice> appSkuPriceList ;
}
