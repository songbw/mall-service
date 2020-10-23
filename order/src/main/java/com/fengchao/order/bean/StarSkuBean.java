package com.fengchao.order.bean;

import com.fengchao.order.model.StarProperty;
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

}
