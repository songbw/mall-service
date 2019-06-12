package com.fengchao.order.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShoppingCartQueryBean extends QueryBean implements Serializable{
    private String openId ;
}
