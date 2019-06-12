package com.fengchao.order.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderQueryBean extends QueryBean implements Serializable{
    private Integer orderId;
}
