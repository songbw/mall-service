package com.fengchao.order.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReceiverQueryBean extends QueryBean implements Serializable{
    private String openId ;
}
