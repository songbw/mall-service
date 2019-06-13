package com.fengchao.order.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressQueryBean extends QueryBean implements Serializable{
    private String pid ;
    private String level ;
    private String cityId;
}
