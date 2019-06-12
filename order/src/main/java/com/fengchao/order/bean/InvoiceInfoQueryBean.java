package com.fengchao.order.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class InvoiceInfoQueryBean extends QueryBean implements Serializable{
    private String openId ;
}
