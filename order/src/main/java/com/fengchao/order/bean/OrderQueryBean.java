package com.fengchao.order.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class OrderQueryBean extends QueryBean implements Serializable{
    private Integer orderId;
    private Integer merchantHeader;
    private String renterHeader;
}
