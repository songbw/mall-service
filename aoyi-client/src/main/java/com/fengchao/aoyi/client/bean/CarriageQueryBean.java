package com.fengchao.aoyi.client.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CarriageQueryBean extends QueryBean implements Serializable{
    private String orderId ;
    private List<CarriageParam> carriages;
}
