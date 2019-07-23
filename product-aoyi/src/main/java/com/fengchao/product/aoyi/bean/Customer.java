package com.fengchao.product.aoyi.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Customer implements Serializable {

    private int type;
    private String[] users;
}
